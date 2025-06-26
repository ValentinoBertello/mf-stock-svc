package com.tesis.merendero.stockservice.controllers;

import com.tesis.merendero.stockservice.dto.responses.OutputResponseDto;
import com.tesis.merendero.stockservice.dto.responses.StockReportDto;
import com.tesis.merendero.stockservice.service.StockReportService;
import jakarta.mail.MessagingException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/report/movements")
public class ReportMovementsController {

    private final StockReportService stockReportService;

    public ReportMovementsController(StockReportService stockReportService) {
        this.stockReportService = stockReportService;
    }

    @GetMapping("/{merenderoId}/supply/{supplyId}/startDate/{startDate}/endDate/{endDate}/groupBy/{groupBy}")
    public ResponseEntity<List<StockReportDto>> getReportMovementsBySupply(
            @PathVariable Long merenderoId,
            @PathVariable Long supplyId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PathVariable String groupBy,
            Authentication authentication
    ) throws AccessDeniedException, MessagingException {
        return ResponseEntity.ok(stockReportService.getReportMovementsBySupply(merenderoId,
                                                                            authentication.getName(),
                                                                            supplyId,
                                                                            startDate,
                                                                            endDate,
                                                                            groupBy ));
    }
}
