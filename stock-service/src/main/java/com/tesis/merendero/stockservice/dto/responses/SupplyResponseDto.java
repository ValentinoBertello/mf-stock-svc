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
public class SupplyResponseDto {

    private Long id;

    private String name;

    private String unit;

    private BigDecimal minQuantity;

    private Long merenderoId;

    private Long categoryId;
}
