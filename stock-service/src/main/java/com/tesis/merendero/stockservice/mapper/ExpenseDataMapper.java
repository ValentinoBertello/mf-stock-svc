package com.tesis.merendero.stockservice.mapper;

import com.tesis.merendero.stockservice.dto.requests.PostExpenseDto;
import com.tesis.merendero.stockservice.dto.responses.ExpenseResponseDto;
import com.tesis.merendero.stockservice.entities.ExpenseEntity;
import com.tesis.merendero.stockservice.entities.TypeExpenseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ExpenseDataMapper {

    public ExpenseResponseDto mapExpenseEntityToExpenseResponse(ExpenseEntity expenseEntity) {
        ExpenseResponseDto response = ExpenseResponseDto.builder()
                .id(expenseEntity.getId())
                .merenderoId(expenseEntity.getMerenderoId())
                .amount(expenseEntity.getAmount())
                .type(expenseEntity.getType().getDescription())
                .expenseDate(expenseEntity.getExpenseDate())
                .build();
        if (Objects.equals(expenseEntity.getType().getDescription(), "Compra de Insumos")) {
            response.setQuantity(expenseEntity.getEntry().getQuantity());
            response.setSupplyId(expenseEntity.getSupply().getId());
            response.setSupplyName(expenseEntity.getSupply().getName());
            response.setUnit(expenseEntity.getSupply().getUnit().name());
        } else {
            response.setQuantity(BigDecimal.ZERO);
            response.setSupplyId(0L);
            response.setSupplyName("NO-APLICA");
            response.setUnit("NO-APLICA");
        }
        return response;
    }

    public List<ExpenseResponseDto> mapExpenseEntitiesToResponses
            (List<ExpenseEntity> expenseEntities) {
        List<ExpenseResponseDto> responses = new ArrayList<>();
        for (ExpenseEntity eE : expenseEntities){
            responses.add(this.mapExpenseEntityToExpenseResponse(eE));
        }
        return responses;
    }

    public ExpenseEntity mapExpenseRequestToEntity(PostExpenseDto requestDTO, TypeExpenseEntity expenseTypeEntity,
                                                   Long merenderoId) {
        return ExpenseEntity.builder()
                .merenderoId(merenderoId)
                .amount(requestDTO.getAmount())
                .entry(null)
                .supply(null)
                .type(expenseTypeEntity)
                .expenseDate(LocalDateTime.now())
                .build();
    }

}
