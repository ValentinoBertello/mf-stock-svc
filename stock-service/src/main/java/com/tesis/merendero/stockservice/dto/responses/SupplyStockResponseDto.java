package com.tesis.merendero.stockservice.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyStockResponseDto {

    private Long id;
    private Long merenderoId;
    private Long supplyId;
    private String supplyName;
    private BigDecimal quantity;

    private String unit;
    private String category;
}
