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
public class StockLoteDto {

    private Long id;
    private BigDecimal initialQuantity;
    private BigDecimal currentQuantity;
    private LocalDate expirationDate;
    private int daysToExpire; // Días restantes hasta vencimiento

}
