package com.tesis.merendero.stockservice.service;

import com.tesis.merendero.stockservice.dto.responses.StockReportDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface StockReportService {
    List<StockReportDto> getReportMovementsBySupply(Long merenderoId, String name, Long supplyId, LocalDate startDate,
                                                    LocalDate endDate, String groupBy);
}
