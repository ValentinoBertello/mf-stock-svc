package com.tesis.merendero.stockservice.service.impl;

import com.tesis.merendero.stockservice.dto.SupplyIdsDto;
import com.tesis.merendero.stockservice.dto.requests.SupplyRequestDto;
import com.tesis.merendero.stockservice.dto.responses.SupplyResponseDto;
import com.tesis.merendero.stockservice.entities.SupplyCategoryEntity;
import com.tesis.merendero.stockservice.entities.SupplyEntity;
import com.tesis.merendero.stockservice.mapper.SupplyDataMapper;
import com.tesis.merendero.stockservice.repository.SupplyCategoryRepository;
import com.tesis.merendero.stockservice.repository.SupplyRepository;
import com.tesis.merendero.stockservice.service.SupplyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplyServiceImpl implements SupplyService {

    private final SupplyRepository supplyRepository;
    private final SupplyDataMapper supplyDataMapper;
    private final SupplyCategoryRepository supplyCategoryRepository;

    public SupplyServiceImpl(SupplyRepository supplyRepository, SupplyDataMapper supplyDataMapper, SupplyCategoryRepository supplyCategoryRepository) {
        this.supplyRepository = supplyRepository;
        this.supplyDataMapper = supplyDataMapper;
        this.supplyCategoryRepository = supplyCategoryRepository;
    }

    @Override
    public List<SupplyResponseDto> getAllGlobalSupplies() {
        List<SupplyEntity> supplyEntities = this.supplyRepository.findAllGlobalSupplies();
        return this.supplyDataMapper.mapSupplyEntitiesToSupplyResponses(supplyEntities);
    }

    @Override
    public List<SupplyResponseDto> getSuppliesByIds(SupplyIdsDto supplyIdsDto) {
        List<SupplyEntity> supplyEntities = this.supplyRepository.findAllById(supplyIdsDto.getIds());
        return this.supplyDataMapper.mapSupplyEntitiesToSupplyResponses(supplyEntities);
    }

    @Override
    public List<SupplyCategoryEntity> getAllSupplyCategories() {
        return this.supplyCategoryRepository.findAll();
    }

    @Override
    public SupplyResponseDto saveSupplyPersonalized(SupplyRequestDto supplyRequestDto, Long categoryId) {
        SupplyCategoryEntity categoryEntity = this.supplyCategoryRepository.findById(categoryId).get();
        SupplyEntity supplyEntity = this.supplyDataMapper.mapSupplyRequestToSupplyEntity(supplyRequestDto,
                                                                                            categoryEntity);
        supplyEntity = this.supplyRepository.save(supplyEntity);
        return this.supplyDataMapper.mapSupplyEntityToSupplyResponse(supplyEntity);
    }
}
