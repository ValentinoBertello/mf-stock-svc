package com.tesis.merendero.stockservice.mapper;

import com.tesis.merendero.stockservice.dto.requests.OutputRequestDto;
import com.tesis.merendero.stockservice.dto.responses.OutputResponseDto;
import com.tesis.merendero.stockservice.entities.SupplyEntity;
import com.tesis.merendero.stockservice.entities.SupplyOutputEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OutputDataMapper {

    public OutputResponseDto mapOutputEntityToResponse(SupplyOutputEntity entity) {
        return OutputResponseDto.builder()
                .id(entity.getId())
                .merenderoId(entity.getMerenderoId())
                .supplyId(entity.getSupply().getId())
                .supplyName(entity.getSupply().getName())
                .category(entity.getSupply().getSupplyCategory().getName())
                .unit(entity.getSupply().getUnit().name())
                .quantity(entity.getQuantity())
                .outputDate(entity.getOutputDate())
                .build();
    }

    public List<OutputResponseDto> mapOutputEntitiesToResponses
            (List<SupplyOutputEntity> entities) {
        List<OutputResponseDto> responses = new ArrayList<>();
        for (SupplyOutputEntity sE : entities){
            responses.add(this.mapOutputEntityToResponse(sE));
        }
        return responses;
    }

    public SupplyOutputEntity mapOutputRequestToEntity(OutputRequestDto requestDto, SupplyEntity supplyEntity) {
        return SupplyOutputEntity.builder()
                .merenderoId(requestDto.getMerenderoId())
                .supply(supplyEntity)
                .quantity(requestDto.getQuantity())
                .build();
    }


}
