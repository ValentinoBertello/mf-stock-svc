package com.tesis.merendero.stockservice.service.impl;

import com.tesis.merendero.stockservice.dto.requests.OutputRequestDto;
import com.tesis.merendero.stockservice.dto.responses.OutputResponseDto;
import com.tesis.merendero.stockservice.dto.responses.StockItemDto;
import com.tesis.merendero.stockservice.entities.SupplyEntity;
import com.tesis.merendero.stockservice.entities.SupplyEntryEntity;
import com.tesis.merendero.stockservice.entities.SupplyLoteEntity;
import com.tesis.merendero.stockservice.entities.SupplyOutputEntity;
import com.tesis.merendero.stockservice.mapper.OutputDataMapper;
import com.tesis.merendero.stockservice.service.StockService;
import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.tesis.merendero.stockservice.repository.OutputRepository;
import com.tesis.merendero.stockservice.repository.SupplyLoteRepository;
import com.tesis.merendero.stockservice.repository.SupplyRepository;
import com.tesis.merendero.stockservice.service.OutputService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OutputServiceImpl implements OutputService {

    private final OutputRepository outputRepository;
    private final SupplyRepository supplyRepository;
    private final OutputDataMapper outputDataMapper;
    private final SupplyLoteRepository loteRepository;
    private final StockService stockService;
    private final JavaMailSender mailSender;


    public OutputServiceImpl(OutputRepository outputRepository, SupplyRepository supplyRepository, OutputDataMapper outputDataMapper, SupplyLoteRepository loteRepository, StockService stockService, JavaMailSender mailSender) {
        this.outputRepository = outputRepository;
        this.supplyRepository = supplyRepository;
        this.outputDataMapper = outputDataMapper;
        this.loteRepository = loteRepository;
        this.stockService = stockService;
        this.mailSender = mailSender;
    }

    @Override
    @Transactional
    public OutputResponseDto saveOutput(Long merenderoId, OutputRequestDto outputRequestDto, String name) throws MessagingException {
        SupplyEntity supplyEntity = this.supplyRepository.findById(outputRequestDto.getSupplyId()).get();

        BigDecimal totalStock = getTotalStock(merenderoId, outputRequestDto.getSupplyId());
        if(totalStock.compareTo(outputRequestDto.getQuantity()) < 0) {
            throw new RuntimeException("Stock insuficiente");
        }

        // Crear y guardar la salida
        SupplyOutputEntity entity = this.outputDataMapper.mapOutputRequestToEntity(outputRequestDto, supplyEntity);
        entity = this.outputRepository.save(entity);

        // Descontar de los lotes
        deductFromLotes(merenderoId, outputRequestDto.getSupplyId(), outputRequestDto.getQuantity());
        StockItemDto stockItemDto = this.stockService.getStockInventoryBySupply(merenderoId, outputRequestDto.getSupplyId());

        if(stockItemDto.getTotalStock().compareTo(supplyEntity.getMinQuantity()) < 0 ) {
            if(supplyEntity.getLastAlertDate() == null || !supplyEntity.getLastAlertDate().equals(LocalDate.now())) {
                this.sendEmail(supplyEntity, name);
                supplyEntity.setLastAlertDate(LocalDate.now());
                this.supplyRepository.save(supplyEntity);
            }
        }

        return this.outputDataMapper.mapOutputEntityToResponse(entity);
    }

    @Override
    public List<OutputResponseDto> getAllOutputs(Long merenderoId, String name) {
        //Obtener todas las salidas del merendero
        List<SupplyOutputEntity> outputs = outputRepository.findByMerenderoId(merenderoId);
        return this.outputDataMapper.mapOutputEntitiesToResponses(outputs);
    }

    private void sendEmail(SupplyEntity supplyEntity, String name) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("merenderofacilteam@gmail.com");

        String testContent = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@600&display=swap');</style>"
                + "</head>"
                + "<body style='margin: 0; padding: 20px; background-color: #f5f5f5; font-family: Arial, sans-serif;'>"
                + "<div style='max-width: 600px; margin: 20px auto; background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>"
                + "<h1 style='font-size: 2.2rem; margin: -8px 0 30px 0; text-align: center; color: #ffb343; font-family: Poppins, sans-serif; font-weight: 600; border-bottom: 3px solid #ffd000; display: inline-block;'>¡Stock Bajo de " + supplyEntity.getName() + "!</h1>"
                + "<p style='font-size: 16px; color: #555; line-height: 1.6;'>¡Atención! Necesitas reponer un insumo</p>"
                + "<p style='font-size: 16px; color: #555; line-height: 1.6;'>Hemos detectado que el siguiente insumo ha bajado por debajo del nivel mínimo establecido: <span style='font-weight: bold;'>" + supplyEntity.getName() + "</span> </p>"
                + "<p style='font-size: 14px; color: #777; line-height: 1.5;'>Recuerda que puedes gestionar tu inventario directamente en la plataforma de Merendero Fácil.</p>"
                 + "<a style='display: inline-block; background: #ffb343; color: white; padding: 12px 27px; border-radius: 5px; font-size: 15px; font-weight: bold; letter-spacing: 2px;'  href='http://localhost:4200/stock/movimientos/entradas'>GESTIONAR INVENTARIO</a>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setTo(name);
        helper.setSubject("¡Alerta! Stock bajo de " + supplyEntity.getName());
        helper.setText(testContent, true);

        // Loggear para debug
        System.out.println("Intentando enviar email a: " + name);
        System.out.println("Contenido del email:\n" + testContent);

        mailSender.send(message);
    }

    private BigDecimal getTotalStock(Long merenderoId, Long supplyId) {
        List<SupplyLoteEntity> lotes = loteRepository
                .findByMerenderoIdAndSupplyIdAndCurrentQuantityGreaterThanOrderByExpirationDateAsc(
                        merenderoId,
                        supplyId,
                        BigDecimal.ZERO
                );
        return lotes.stream()
                .map(SupplyLoteEntity::getCurrentQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void deductFromLotes(Long merenderoId, Long supplyId, BigDecimal requiredQuantity) {
        List<SupplyLoteEntity> lotes = loteRepository
                .findByMerenderoIdAndSupplyIdAndCurrentQuantityGreaterThanOrderByExpirationDateAsc(
                        merenderoId,
                        supplyId,
                        BigDecimal.ZERO
                );

        BigDecimal remaining = requiredQuantity;

        for(SupplyLoteEntity lote : lotes) {
            if(remaining.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            BigDecimal toDeduct = lote.getCurrentQuantity().min(remaining);
            lote.setCurrentQuantity(lote.getCurrentQuantity().subtract(toDeduct));
            remaining = remaining.subtract(toDeduct);

            lote = loteRepository.save(lote);

            System.out.println("lote Current Quantity: " + lote.getCurrentQuantity());
            System.out.println("BigDecimal ZERO: " + BigDecimal.ZERO);

            if(lote.getCurrentQuantity().compareTo(BigDecimal.valueOf(1)) < 0) {
                System.out.println("lote Id" + lote.getId());
                loteRepository.deleteById(lote.getId());
            }
        }
    }

}
