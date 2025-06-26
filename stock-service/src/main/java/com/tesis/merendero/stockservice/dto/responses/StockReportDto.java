package com.tesis.merendero.stockservice.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockReportDto {

    /*
    Si GroupBy="day" → fecha exacta (por ejemplo: 2025-06-11)
    Si GroupBy="week" → fecha del lunes de esa semana (2025-06-08)
    Si GroupBy="month" → fecha del primer dia del mes (2025-06-01)
    * */
    private LocalDate date;

    private BigDecimal donationQty;
    private BigDecimal purchaseQty;
    private BigDecimal outputQty;
}
