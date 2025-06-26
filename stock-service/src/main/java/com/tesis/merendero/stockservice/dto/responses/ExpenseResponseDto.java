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
public class ExpenseResponseDto {

    private Long id;

    private Long merenderoId;

    private BigDecimal amount;

    private Long supplyId;

    private String supplyName;

    private BigDecimal quantity;

    private String unit;

    private String type;

    private LocalDateTime expenseDate;

}
