package com.tesis.merendero.stockservice.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputResponseDto {

    private Long id;

    private Long merenderoId;

    private Long supplyId;

    private String supplyName;

    private String category;

    private String unit;

    private BigDecimal quantity;

    private LocalDateTime outputDate;

}
