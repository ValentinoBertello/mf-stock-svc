package com.tesis.merendero.stockservice.service.impl;

import com.tesis.merendero.stockservice.dto.requests.ExpenseWithEntryRequest;
import com.tesis.merendero.stockservice.dto.requests.PostEntryDto;
import com.tesis.merendero.stockservice.dto.requests.PostExpenseDto;
import com.tesis.merendero.stockservice.dto.responses.ExpenseResponseDto;
import com.tesis.merendero.stockservice.entities.*;
import com.tesis.merendero.stockservice.enums.EntryType;
import com.tesis.merendero.stockservice.mapper.EntryDataMapper;
import com.tesis.merendero.stockservice.mapper.ExpenseDataMapper;
import com.tesis.merendero.stockservice.repository.*;
import com.tesis.merendero.stockservice.service.ExpenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseDataMapper expenseDataMapper;
    private final TypeExpenseRepository typeExpenseRepository;
    private final SupplyRepository supplyRepository;
    private final EntryRepository entryRepository;
    private final EntryDataMapper entryDataMapper;
    private final SupplyLoteRepository loteRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ExpenseDataMapper expenseDataMapper, TypeExpenseRepository typeExpenseRepository, SupplyRepository supplyRepository, EntryRepository entryRepository, EntryDataMapper entryDataMapper, SupplyLoteRepository loteRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseDataMapper = expenseDataMapper;
        this.typeExpenseRepository = typeExpenseRepository;
        this.supplyRepository = supplyRepository;
        this.entryRepository = entryRepository;
        this.entryDataMapper = entryDataMapper;
        this.loteRepository = loteRepository;
    }

    @Override
    public List<ExpenseResponseDto> getAllExpenses(Long merenderoId, String name) {
        List<ExpenseEntity> entities = this.expenseRepository.findByMerenderoId(merenderoId);
        return this.expenseDataMapper.mapExpenseEntitiesToResponses(entities);
    }

    @Override
    @Transactional
    public ExpenseResponseDto saveExpense(Long merenderoId, String name, ExpenseWithEntryRequest requestDTO) {
        PostExpenseDto expenseRequest = requestDTO.getExpense();
        PostEntryDto entryRequest = requestDTO.getEntry();
        TypeExpenseEntity expenseTypeEntity = typeExpenseRepository.findById(requestDTO.getExpense().getTypeExpenseId()).get();


        if (entryRequest == null) {
            ExpenseEntity expense = this.expenseDataMapper.mapExpenseRequestToEntity(requestDTO.getExpense(), expenseTypeEntity,
                    merenderoId);
            expense = this.expenseRepository.save(expense);
            return this.expenseDataMapper.mapExpenseEntityToExpenseResponse(expense);
        } else {
            // Validar fecha de vencimiento
            if(entryRequest.getExpirationDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Fecha de vencimiento inválida");
            }

            SupplyEntity supplyEntity = this.supplyRepository.findById(entryRequest.getSupplyId()).get();
            System.out.println("supplyEntity.getName():  " + supplyEntity.getName());
            SupplyEntryEntity entryEntity = this.createEntryEntity(merenderoId, entryRequest, supplyEntity);
            entryEntity = this.entryRepository.save(entryEntity);

            // Crear y guardar el lote asociado
            SupplyLoteEntity lote = SupplyLoteEntity.builder()
                    .merenderoId(merenderoId)
                    .supply(supplyEntity)
                    .entry(entryEntity)
                    .initialQuantity(entryRequest.getQuantity())
                    .currentQuantity(entryRequest.getQuantity())
                    .expirationDate(entryRequest.getExpirationDate())
                    .notified(false)
                    .build();
            lote = this.loteRepository.save(lote);


            ExpenseEntity expense = ExpenseEntity.builder()
                    .merenderoId(merenderoId)
                    .amount(requestDTO.getExpense().getAmount())
                    .entry(entryEntity)
                    .supply(supplyEntity)
                    .type(expenseTypeEntity)
                    .expenseDate(entryEntity.getEntryDate())
                    .build();
            expense = this.expenseRepository.save(expense);
            return this.expenseDataMapper.mapExpenseEntityToExpenseResponse(expense);
        }
    }

    @Override
    public List<TypeExpenseEntity> getAllExpenseTypes() {
        return this.typeExpenseRepository.findAll();
    }

    private SupplyEntryEntity createEntryEntity(Long merenderoId,PostEntryDto entryRequest
            , SupplyEntity supplyEntity) {
        return SupplyEntryEntity.builder()
                .merenderoId(merenderoId)
                .supply(supplyEntity)
                .quantity(entryRequest.getQuantity())
                .entryDate(LocalDateTime.now())
                .entryType(EntryType.PURCHASE)
                .build();
    }
}
