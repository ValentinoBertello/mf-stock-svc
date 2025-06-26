package com.tesis.merendero.stockservice.mapper;

import com.tesis.merendero.stockservice.dto.responses.SupplyStockResponseDto;
import com.tesis.merendero.stockservice.entities.SupplyStockEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component
public class SupplyStockDataMapper {

//    public SupplyStockResponseDto mapSupplyStockEntityToResponse(SupplyStockEntity entity) {
//        return SupplyStockResponseDto.builder()
//                .id(entity.getId())
//                .supplyId(entity.getSupply().getId())
//                .merenderoId(entity.getMerenderoId())
//                .supplyName(entity.getSupply().getName())
//                .quantity(entity.getQuantity())
//
//                .category(entity.getSupply().getSupplyCategory().getName())
//                .unit(entity.getSupply().getUnit().name())
//
//                .build();
//    }
//
//    public List<SupplyStockResponseDto> mapSupplyStockEntitiesToResponses
//            (List<SupplyStockEntity> entities) {
//        List<SupplyStockResponseDto> responses = new ArrayList<>();
//        for (SupplyStockEntity sE : entities){
//            responses.add(this.mapSupplyStockEntityToResponse(sE));
//        }
//        return responses;
//    }

}
