package com.tesis.merendero.stockservice.controllers;

import com.tesis.merendero.stockservice.dto.requests.ExpenseWithEntryRequest;
import com.tesis.merendero.stockservice.dto.requests.PostExpenseDto;
import com.tesis.merendero.stockservice.dto.responses.ExpenseResponseDto;
import com.tesis.merendero.stockservice.entities.TypeExpenseEntity;
import com.tesis.merendero.stockservice.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/save/{merenderoId}")
    public ResponseEntity<ExpenseResponseDto> saveExpense(
            @PathVariable Long merenderoId,
            Authentication authentication,
            @RequestBody ExpenseWithEntryRequest requestDTO
            ) throws AccessDeniedException {
        return ResponseEntity.ok(expenseService.saveExpense(merenderoId, authentication.getName(), requestDTO));
    }

    @GetMapping("/all/{merenderoId}")
    public ResponseEntity<List<ExpenseResponseDto>> getAllExpenses(
            @PathVariable Long merenderoId,
            Authentication authentication
    ) throws AccessDeniedException {
        return ResponseEntity.ok(expenseService.getAllExpenses(merenderoId, authentication.getName()));
    }

    @GetMapping("/all-types")
    public ResponseEntity<List<TypeExpenseEntity>> getAllExpenseTypes() {
        return ResponseEntity.ok(expenseService.getAllExpenseTypes());
    }
}
