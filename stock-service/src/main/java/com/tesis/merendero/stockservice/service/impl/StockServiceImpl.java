package com.tesis.merendero.stockservice.service.impl;

import com.tesis.merendero.stockservice.dto.responses.StockItemDto;
import com.tesis.merendero.stockservice.dto.responses.StockLoteDto;
import com.tesis.merendero.stockservice.entities.SupplyEntity;
import com.tesis.merendero.stockservice.entities.SupplyLoteEntity;
import com.tesis.merendero.stockservice.repository.SupplyLoteRepository;
import com.tesis.merendero.stockservice.repository.SupplyRepository;
import com.tesis.merendero.stockservice.service.StockService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class StockServiceImpl implements StockService {

    private final SupplyLoteRepository loteRepository;
    private final SupplyRepository supplyRepository;

    public StockServiceImpl(SupplyLoteRepository loteRepository, SupplyRepository supplyRepository) {
        this.loteRepository = loteRepository;
        this.supplyRepository = supplyRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<StockItemDto> getStockInventory(Long merenderoId) {


        //0. Obtener todos los lotes activos del merendero
        List<SupplyLoteEntity> lotes = this.loteRepository
                .findByMerenderoIdAndCurrentQuantityGreaterThan(merenderoId, BigDecimal.ZERO);

        //0. Creamos un mapa para almacenar el stock total por insumo
        // key: id del insumo, Value: stock total acumulado de ese insumo
        Map<Long, BigDecimal> stockPorInsumo = new HashMap<>();

        //0. Creamos un mapa para almacenar la fecha de vencimiento mas cercana por insumo
        // Key: id del insumo, Value: Fecha de vencimiento mas próxima
        Map<Long, LocalDate> proximoVencimiento = new HashMap<>();

        //0. Recorremos todos los lotes
        for(SupplyLoteEntity lote: lotes) {
            Long supplyId = lote.getSupply().getId();
            BigDecimal cantidadActual = lote.getCurrentQuantity();
            LocalDate fechaVencimiento = lote.getExpirationDate();

            //0. Actualizamos el stock total para este insumo
            if (stockPorInsumo.containsKey(supplyId)) {
                //Si ya existe sumamos la cantidad actual
                BigDecimal stockActual = stockPorInsumo.get(supplyId);
                stockPorInsumo.put(supplyId, stockActual.add(cantidadActual));

            } else {
                // Si es la primera vez que vemos este insumo, iniciamos el stock
                stockPorInsumo.put(supplyId, cantidadActual);
            }

            //0. Actualizamos la fecha de vencimiento mas cercana a la fecha
            if (proximoVencimiento.containsKey(supplyId)) {
                // Comparamos la fecha existente
                LocalDate fechaExistente = proximoVencimiento.get(supplyId);
                if(fechaVencimiento.isBefore(fechaExistente)) {
                    proximoVencimiento.put(supplyId, fechaVencimiento);
                }
            } else {
                // Si es la primera vez establecemos esta fecha
                proximoVencimiento.put(supplyId, fechaVencimiento);
            }
        }

        //0. Obtenemos todos los ids de insumos que tenemos en stock (en los "Set se eliminan las repeticiones")
        Set<Long> supplyIds = stockPorInsumo.keySet();

        List<SupplyEntity> supplyEntities = this.supplyRepository.findByMerenderoIdAndActiveTrue(merenderoId);

        //0. Buscamos los detalles completos de los insumos
        List<SupplyEntity> supplies = this.supplyRepository.findAllById(supplyIds);

        //0. Construímos la respuesta final
        List<StockItemDto> inventory = new ArrayList<>();

        for(SupplyEntity supply : supplyEntities) {
            Long supplyId = supply.getId();

            //0. Creamos el dto
            StockItemDto item = new StockItemDto();
            item.setSupplyId(supplyId);
            item.setSupplyName(supply.getName());
            item.setUnit(supply.getUnit().name());
            item.setCategory(supply.getSupplyCategory().getName());
            item.setMinQuantity(supply.getMinQuantity());

            if(!supplyIds.contains(supplyId)) {
                item.setTotalStock(BigDecimal.ZERO);
                item.setNextExpiration(null);
                item.setHasLotes(false);
            } else {
                item.setTotalStock(stockPorInsumo.get(supplyId));
                item.setNextExpiration(proximoVencimiento.get(supplyId));
                item.setHasLotes(true);
            }

            inventory.add(item);
        }
        return inventory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockLoteDto> getLotesBySupply(Long merenderoId, Long supplyId) {
        return this.loteRepository.
                findByMerenderoIdAndSupplyIdAndCurrentQuantityGreaterThanOrderByExpirationDateAsc
                        (merenderoId, supplyId, BigDecimal.ZERO)
                .stream()
                .map(this::mapToLoteDto)
                .collect(Collectors.toList());
    }

    @Override
    public StockItemDto getStockInventoryBySupply(Long merenderoId, Long supplyId) {
        // 1. Obtener el insumo
        SupplyEntity supply = supplyRepository.findById(supplyId)
                .orElseThrow(() -> new EntityNotFoundException("Insumo no encontrado con ID: " + supplyId));

        // 2. Obtener lotes activos para este insumo específico
        List<SupplyLoteEntity> lotes = loteRepository.findByMerenderoIdAndSupplyIdAndCurrentQuantityGreaterThan(
                merenderoId, supplyId, BigDecimal.ZERO);

        // 3. Calcular stock total y próximo vencimiento
        BigDecimal totalStock = BigDecimal.ZERO;
        LocalDate nextExpiration = null;
        boolean hasLotes = !lotes.isEmpty();

        for (SupplyLoteEntity lote : lotes) {
            totalStock = totalStock.add(lote.getCurrentQuantity());

            // Calcular el vencimiento más próximo
            if (lote.getExpirationDate() != null) {
                if (nextExpiration == null || lote.getExpirationDate().isBefore(nextExpiration)) {
                    nextExpiration = lote.getExpirationDate();
                }
            }
        }

        // 4. Construir y retornar el DTO
        return StockItemDto.builder()
                .supplyId(supplyId)
                .supplyName(supply.getName())
                .unit(supply.getUnit().name())
                .category(supply.getSupplyCategory().getName())
                .minQuantity(supply.getMinQuantity())
                .totalStock(totalStock)
                .nextExpiration(nextExpiration)
                .hasLotes(hasLotes)
                .build();
    }

    private StockLoteDto mapToLoteDto(SupplyLoteEntity lote) {
        StockLoteDto dto = new StockLoteDto();
        dto.setId(lote.getId());
        dto.setInitialQuantity(lote.getInitialQuantity());
        dto.setCurrentQuantity(lote.getCurrentQuantity());
        dto.setExpirationDate(lote.getExpirationDate());

        long dias = ChronoUnit.DAYS.between(LocalDate.now(), lote.getExpirationDate());
        dto.setDaysToExpire((int) dias);

        return dto;
    }
}
