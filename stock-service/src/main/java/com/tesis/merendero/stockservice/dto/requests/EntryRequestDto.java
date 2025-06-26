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
public class EntryRequestDto {

    private Long merenderoId;

    private Long supplyId;

    private BigDecimal quantity;

    private String entryType; //DONATION | PURCHASE

    private LocalDate expirationDate; // Nuevo campo
}
