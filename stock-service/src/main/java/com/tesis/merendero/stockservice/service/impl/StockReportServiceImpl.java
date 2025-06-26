package com.tesis.merendero.stockservice.service.impl;

import com.tesis.merendero.stockservice.dto.responses.StockReportDto;
import com.tesis.merendero.stockservice.entities.SupplyEntryEntity;
import com.tesis.merendero.stockservice.entities.SupplyOutputEntity;
import com.tesis.merendero.stockservice.enums.EntryType;
import com.tesis.merendero.stockservice.repository.EntryRepository;
import com.tesis.merendero.stockservice.repository.OutputRepository;
import com.tesis.merendero.stockservice.service.StockReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class StockReportServiceImpl implements StockReportService {

    private final EntryRepository entryRepository;
    private final OutputRepository outputRepository;

    public StockReportServiceImpl(EntryRepository entryRepository, OutputRepository outputRepository) {
        this.entryRepository = entryRepository;
        this.outputRepository = outputRepository;
    }

    @Override
    public List<StockReportDto> getReportMovementsBySupply(Long merenderoId, String name, Long supplyId,
                                                           LocalDate startDate, LocalDate endDate, String groupBy) {
        // Este mapa almacenará un StockReportDto por cada "bucket" de fecha (dia, lunes de semana o mes)
        Map<LocalDate, StockReportDto> responsesMap = new HashMap<>();

        LocalDateTime desde = startDate.atStartOfDay();
        LocalDateTime hasta = endDate.atTime(LocalTime.MAX);

        // Obtenemos todas las entradas de suministros para el insumo seleccionado y en el rango deseado
        List<SupplyEntryEntity> entryEntities = this.entryRepository.findByDatesAndSupply(desde, hasta,
                                                                                      supplyId, merenderoId);
        // Obtenemos todas las salidas de suministros para el insumo seleccionado y en el rango deseado
        List<SupplyOutputEntity> outputEntities = this.outputRepository.findByDatesAndSupply(desde, hasta,
                supplyId, merenderoId);


        // ----------------------------------------
        // 1) AGRUPAR POR DÍA
        // ----------------------------------------
        if (Objects.equals(groupBy, "day")) {
            // Recorremos cada entrada y la asociamos al StockReportDto del mismo día
            for (SupplyEntryEntity e : entryEntities) {

                LocalDate bucketDate = LocalDate.from(e.getEntryDate());

                // Si aún no existe un DTO para esa fecha, lo creamos e insertamos en el map
                if (!responsesMap.containsKey(bucketDate)) {

                    StockReportDto dto = new StockReportDto();
                    dto.setDate(bucketDate);
                    // Inicializamos los montos a cero para evitar nulls
                    dto.setDonationQty(BigDecimal.ZERO);
                    dto.setPurchaseQty(BigDecimal.ZERO);
                    dto.setOutputQty(BigDecimal.ZERO);

                    // Dependiendo del tipo de entrada (DONATION o PURCHASE), asignamos la cantidad
                    if (e.getEntryType().name().equals(EntryType.DONATION.name())) {
                        dto.setDonationQty(e.getQuantity());
                    } else {
                        dto.setPurchaseQty(e.getQuantity());
                    }

                    // Lo guardamos al dto recien creado en el map para futuras iteraciones
                    responsesMap.put(LocalDate.from(e.getEntryDate()), dto);
                } else {
                    // Si ya existe un dto para esa fecha, lo obtenemos y acumulamos la cantidad
                    StockReportDto dto = responsesMap.get(bucketDate);
                    if (e.getEntryType() == EntryType.DONATION) {
                        dto.setDonationQty(dto.getDonationQty().add(e.getQuantity()));
                    } else {
                        dto.setPurchaseQty(dto.getPurchaseQty().add(e.getQuantity()));
                    }
                    //No es necesario volver a ponerlo en el mapa, porque ya estamos modificando
                    //La instancia creada
                }
            }

            // Después de procesar entradas, procesamos salidas (similar)
            for (SupplyOutputEntity o : outputEntities) {
                LocalDate bucketDate = LocalDate.from(o.getOutputDate());

                if (!responsesMap.containsKey(bucketDate)) {
                    StockReportDto dto = new StockReportDto();
                    dto.setDate(bucketDate);
                    dto.setDonationQty(BigDecimal.ZERO);
                    dto.setPurchaseQty(BigDecimal.ZERO);
                    dto.setOutputQty(BigDecimal.ZERO);

                    dto.setOutputQty(o.getQuantity());
                    responsesMap.put(bucketDate, dto);

                } else {
                    StockReportDto dto = responsesMap.get(bucketDate);
                    dto.setOutputQty(dto.getOutputQty().add(o.getQuantity()));
                }
            }

            // Convertimos el mapa a lista y ordenamos por fecha ascendente
            List<StockReportDto> responses = new ArrayList<>(responsesMap.values());
            responses.sort(Comparator.comparing(StockReportDto::getDate));
            return responses;
        }

        // ----------------------------------------
        // 2) AGRUPAR POR SEMANA (lunes de cada semana)
        // ----------------------------------------
        if (Objects.equals(groupBy, "week")) {
            for (SupplyEntryEntity e : entryEntities) {
                // Convertimos entryDate a LocalDate
                LocalDate entryDate = LocalDate.from(e.getEntryDate());

                // Ajustamos la fecha al lunes de la semana en la que esté
                //(previo o mismo si ya es lunes)
                LocalDate bucketDate = entryDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));

                // Si no existe un DTO para ese lunes, creamos uno nuevo
                if (!responsesMap.containsKey(bucketDate)) {
                    StockReportDto dto = StockReportDto.builder()
                            .date(bucketDate)
                            .donationQty(BigDecimal.ZERO)
                            .purchaseQty(BigDecimal.ZERO)
                            .outputQty(BigDecimal.ZERO)
                            .build();

                    if (e.getEntryType() == EntryType.DONATION) {
                        dto.setDonationQty(e.getQuantity());
                    } else {
                        dto.setPurchaseQty(e.getQuantity());
                    }

                    responsesMap.put(bucketDate, dto);

                } else {
                    // Si ya existía, simplemente acumulamos
                    StockReportDto dto = responsesMap.get(bucketDate);
                    if (e.getEntryType() == EntryType.DONATION) {
                        dto.setDonationQty(dto.getDonationQty().add(e.getQuantity()));
                    } else {
                        dto.setPurchaseQty(dto.getPurchaseQty().add(e.getQuantity()));
                    }
                }
            }

            // Procesamos salidas en el mismo “bucket” (lunes) correspondiente
            for (SupplyOutputEntity o : outputEntities) {
                LocalDate outputDate = LocalDate.from(o.getOutputDate());
                LocalDate bucketDate = outputDate.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));

                if (!responsesMap.containsKey(bucketDate)) {
                    StockReportDto dto = StockReportDto.builder()
                            .date(bucketDate)
                            .donationQty(BigDecimal.ZERO)
                            .purchaseQty(BigDecimal.ZERO)
                            .outputQty(BigDecimal.ZERO)
                            .build();

                    dto.setOutputQty(o.getQuantity());
                    responsesMap.put(bucketDate, dto);

                } else {
                    StockReportDto dto = responsesMap.get(bucketDate);
                    dto.setOutputQty(dto.getOutputQty().add(o.getQuantity()));
                }
            }

            // Convertimos a lista y ordenamos por fecha (lunes) ascendente
            List<StockReportDto> responses = new ArrayList<>(responsesMap.values());
            responses.sort(Comparator.comparing(StockReportDto::getDate));
            return responses;
        }

        // ----------------------------------------
        // 3) AGRUPAR POR MES (primer día del mes)
        // ----------------------------------------
        if (Objects.equals(groupBy, "month")) {
            for (SupplyEntryEntity e: entryEntities) {
                //convertimos entryDate a localDate
                LocalDate entryDate = LocalDate.from(e.getEntryDate());

                //ajustamos el primer dia del mes de entryDate
                LocalDate bucketDate = entryDate.withDayOfMonth(1);

                if (!responsesMap.containsKey(bucketDate)) {
                    StockReportDto dto = StockReportDto.builder()
                            .date(bucketDate)
                            .donationQty(BigDecimal.ZERO)
                            .purchaseQty(BigDecimal.ZERO)
                            .outputQty(BigDecimal.ZERO)
                            .build();

                    if (e.getEntryType() == EntryType.DONATION) {
                        dto.setDonationQty(e.getQuantity());
                    } else {
                        dto.setPurchaseQty(e.getQuantity());
                    }

                    responsesMap.put(bucketDate, dto);

                } else {
                    StockReportDto dto = responsesMap.get(bucketDate);
                    if (e.getEntryType() == EntryType.DONATION) {
                        dto.setDonationQty(dto.getDonationQty().add(e.getQuantity()));
                    } else {
                        dto.setPurchaseQty(dto.getPurchaseQty().add(e.getQuantity()));
                    }
                }
            }

            // Procesamos salidas acumulando en el mismo mes (primer día)
            for (SupplyOutputEntity o : outputEntities) {
                LocalDate outputDate = LocalDate.from(o.getOutputDate());
                LocalDate bucketDate = outputDate.withDayOfMonth(1);

                if (!responsesMap.containsKey(bucketDate)) {
                    StockReportDto dto = StockReportDto.builder()
                            .date(bucketDate)
                            .donationQty(BigDecimal.ZERO)
                            .purchaseQty(BigDecimal.ZERO)
                            .outputQty(BigDecimal.ZERO)
                            .build();

                    dto.setOutputQty(o.getQuantity());
                    responsesMap.put(bucketDate, dto);

                } else {
                    StockReportDto dto = responsesMap.get(bucketDate);
                    dto.setOutputQty(dto.getOutputQty().add(o.getQuantity()));
                }
            }

            // Convertimos a lista y ordenamos por fecha (primer día de mes) ascendente
            List<StockReportDto> responses = new ArrayList<>(responsesMap.values());
            responses.sort(Comparator.comparing(StockReportDto::getDate));
            return responses;
        }

        // Si groupBy no coincide con “day”, “week” ni “month”, devolvemos lista vacía
        return Collections.emptyList();
    }
}
