package com.tesis.merendero.stockservice.dto.requests;

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
public class PostExpenseDto {

    private BigDecimal amount;
    private Long typeExpenseId;
    private LocalDateTime expenseDate;


}
