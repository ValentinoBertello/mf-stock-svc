package com.tesis.merendero.stockservice.mapper;

import com.tesis.merendero.stockservice.dto.requests.SupplyRequestDto;
import com.tesis.merendero.stockservice.dto.responses.SupplyResponseDto;
import com.tesis.merendero.stockservice.entities.SupplyCategoryEntity;
import com.tesis.merendero.stockservice.entities.SupplyEntity;
import com.tesis.merendero.stockservice.enums.Unit;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class SupplyDataMapper {

    public SupplyResponseDto mapSupplyEntityToSupplyResponse(SupplyEntity supplyEntity) {
        return SupplyResponseDto.builder()
                .id(supplyEntity.getId())
                .name(supplyEntity.getName())
                .unit(supplyEntity.getUnit().name())
                .minQuantity(supplyEntity.getMinQuantity())
                .merenderoId(supplyEntity.getMerenderoId())
                .categoryId(supplyEntity.getSupplyCategory().getId())
                .build();
    }

    public List<SupplyResponseDto> mapSupplyEntitiesToSupplyResponses
            (List<SupplyEntity> supplyEntities) {
        List<SupplyResponseDto> responses = new ArrayList<>();
        for (SupplyEntity sE : supplyEntities){
            responses.add(this.mapSupplyEntityToSupplyResponse(sE));
        }
        return responses;
    }

    public SupplyEntity mapSupplyRequestToSupplyEntity(SupplyRequestDto supplyRequestDto, SupplyCategoryEntity category) {
        return SupplyEntity.builder()
                .name(supplyRequestDto.getName())
                .unit(Unit.valueOf(supplyRequestDto.getUnit()))
                .minQuantity(supplyRequestDto.getMinQuantity())
                .lastAlertDate(LocalDate.now())
                .active(true)
                .supplyCategory(category)
                .merenderoId(supplyRequestDto.getMerenderoId())
                .build();
    }

}
