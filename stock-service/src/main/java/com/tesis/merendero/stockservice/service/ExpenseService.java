package com.tesis.merendero.stockservice.service;

import com.tesis.merendero.stockservice.dto.requests.ExpenseWithEntryRequest;
import com.tesis.merendero.stockservice.dto.requests.PostExpenseDto;
import com.tesis.merendero.stockservice.dto.responses.ExpenseResponseDto;
import com.tesis.merendero.stockservice.entities.TypeExpenseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpenseService {
    List<ExpenseResponseDto> getAllExpenses(Long merenderoId, String name);

    ExpenseResponseDto saveExpense(Long merenderoId, String name, ExpenseWithEntryRequest requestDTO);

    List<TypeExpenseEntity> getAllExpenseTypes();
}
