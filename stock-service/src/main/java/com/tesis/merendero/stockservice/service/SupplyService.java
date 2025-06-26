package com.tesis.merendero.stockservice.service;

import com.tesis.merendero.stockservice.dto.SupplyIdsDto;
import com.tesis.merendero.stockservice.dto.requests.SupplyRequestDto;
import com.tesis.merendero.stockservice.dto.responses.SupplyResponseDto;
import com.tesis.merendero.stockservice.entities.SupplyCategoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SupplyService {
    List<SupplyResponseDto> getAllGlobalSupplies();

    List<SupplyResponseDto> getSuppliesByIds(SupplyIdsDto supplyIds);

    List<SupplyCategoryEntity> getAllSupplyCategories();

    SupplyResponseDto saveSupplyPersonalized(SupplyRequestDto supplyRequestDto, Long categoryId);
}
