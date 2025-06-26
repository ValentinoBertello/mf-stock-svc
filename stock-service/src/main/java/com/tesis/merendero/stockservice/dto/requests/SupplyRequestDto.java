package com.tesis.merendero.stockservice.dto.requests;

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
public class SupplyRequestDto {

    private String name;

    private String unit;

    private BigDecimal minQuantity;

    private LocalDate lastAlertDate;

    private Long merenderoId;

    private Long supplyCategoryId;
}
