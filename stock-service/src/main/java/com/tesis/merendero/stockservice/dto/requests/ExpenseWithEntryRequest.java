package com.tesis.merendero.stockservice.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseWithEntryRequest {
    private PostExpenseDto expense;
    private PostEntryDto entry;
}
