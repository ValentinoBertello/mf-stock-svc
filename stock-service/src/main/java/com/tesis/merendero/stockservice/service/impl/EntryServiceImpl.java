package com.tesis.merendero.stockservice.service.impl;

import com.tesis.merendero.stockservice.dto.requests.EntryRequestDto;
import com.tesis.merendero.stockservice.dto.requests.EntryWithExpenseRequest;
import com.tesis.merendero.stockservice.dto.requests.ExpenseRequestDto;
import com.tesis.merendero.stockservice.dto.responses.EntryResponseDto;
import com.tesis.merendero.stockservice.entities.*;
import com.tesis.merendero.stockservice.enums.EntryType;
import com.tesis.merendero.stockservice.mapper.EntryDataMapper;
import com.tesis.merendero.stockservice.repository.*;
import com.tesis.merendero.stockservice.service.EntryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntryServiceImpl implements EntryService {

    private final SupplyRepository supplyRepository;
    private final EntryRepository entryRepository;
    private final EntryDataMapper entryDataMapper;
    private final SupplyLoteRepository loteRepository;
    private final ExpenseRepository expenseRepository;
    private final TypeExpenseRepository typeExpenseRepository;

    public EntryServiceImpl(SupplyRepository supplyRepository, EntryRepository entryRepository, EntryDataMapper entryDataMapper, SupplyLoteRepository loteRepository, ExpenseRepository expenseRepository, TypeExpenseRepository typeExpenseRepository) {
        this.supplyRepository = supplyRepository;
        this.entryRepository = entryRepository;
        this.entryDataMapper = entryDataMapper;
        this.loteRepository = loteRepository;
        this.expenseRepository = expenseRepository;
        this.typeExpenseRepository = typeExpenseRepository;
    }

    @Override
    @Transactional
    public EntryResponseDto saveEntry(Long merenderoId, EntryWithExpenseRequest request, String name) {
        EntryRequestDto entryRequestDto = request.getEntry();
        ExpenseRequestDto expenseRequest = request.getExpense();

        // Validar fecha de vencimiento
        if(entryRequestDto.getExpirationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha de vencimiento inválida");
        }

        SupplyEntity supplyEntity = supplyRepository.findById(entryRequestDto.getSupplyId())
                .orElseThrow(() -> new IllegalArgumentException("Insumo no encontrado"));


        // Crear y guardar la entrada
        SupplyEntryEntity entryEntity = this.entryDataMapper.mapEntryRequestToEntity(entryRequestDto, supplyEntity);
        entryEntity = this.entryRepository.save(entryEntity);

        // Crear y guardar el lote asociado
        SupplyLoteEntity lote = this.entryDataMapper.mapToLoteEntity(entryEntity, entryRequestDto, supplyEntity);
        lote = this.loteRepository.save(lote);

        TypeExpenseEntity typeExpenseEntity = this.typeExpenseRepository.findByDescription("Compra de Insumos");

        //Si es una compra crear el gasto asociado
        if (Objects.equals(entryRequestDto.getEntryType(), EntryType.PURCHASE.name()) && expenseRequest != null) {
            ExpenseEntity expense = new ExpenseEntity();
            expense.setMerenderoId(entryRequestDto.getMerenderoId());
            expense.setAmount(expenseRequest.getAmount());
            expense.setType(typeExpenseEntity);
            expense.setSupply(supplyEntity);
            expense.setExpenseDate(entryEntity.getEntryDate());
            expense.setEntry(entryEntity);
            this.expenseRepository.save(expense);
        }


        return this.entryDataMapper.mapEntryEntityToResponse(entryEntity);
    }

    @Override
    public List<EntryResponseDto> getAllEntries(Long merenderoId, String name) {
        //Obtener todas las entradas del merendero
        List<SupplyEntryEntity> entries = entryRepository.findByMerenderoId(merenderoId);

        //Obtener los ids de las entradas
        List<Long> entryIds = entries.stream()
                .map(SupplyEntryEntity::getId)
                .collect(Collectors.toList());

        //Obtener gastos asociados a esas entradas
        Map<Long, BigDecimal> expensesMap = expenseRepository.findByEntryIdIn(entryIds)
                .stream()
                .collect(Collectors.toMap(
                        exp -> exp.getEntry().getId(),
                        ExpenseEntity::getAmount
                ));
        // Mapear a DTOs
        return entries.stream().map(entry -> {
            EntryResponseDto dto = entryDataMapper.mapEntryEntityToResponse(entry);

            // Asignar costo si existe
            if(expensesMap.containsKey(entry.getId())) {
                dto.setCost(expensesMap.get(entry.getId()));
            }

            return dto;
        }).collect(Collectors.toList());
    }



}
