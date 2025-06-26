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
public class StockItemDto {

    private Long supplyId;
    private String supplyName;
    private BigDecimal minQuantity;
    private String unit;
    private String category;
    private BigDecimal totalStock;
    private LocalDate nextExpiration; // Fecha del vencimiento mas cercano
    private boolean hasLotes;

}
