package com.tesis.merendero.stockservice.mapper;

import com.tesis.merendero.stockservice.dto.requests.EntryRequestDto;
import com.tesis.merendero.stockservice.dto.responses.EntryResponseDto;
import com.tesis.merendero.stockservice.entities.SupplyEntity;
import com.tesis.merendero.stockservice.entities.SupplyEntryEntity;
import com.tesis.merendero.stockservice.entities.SupplyLoteEntity;
import com.tesis.merendero.stockservice.enums.EntryType;
import com.tesis.merendero.stockservice.repository.EntryRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EntryDataMapper {

    private final EntryRepository entryRepository;

    public EntryDataMapper(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public EntryResponseDto mapEntryEntityToResponse(SupplyEntryEntity entity) {
        return EntryResponseDto.builder()
                .id(entity.getId())
                .merenderoId(entity.getMerenderoId())
                .supplyId(entity.getSupply().getId())
                .supplyName(entity.getSupply().getName())
                .category(entity.getSupply().getSupplyCategory().getName())
                .unit(entity.getSupply().getUnit().name())
                .quantity(entity.getQuantity())
                .entryDate(entity.getEntryDate())
                .entryType(entity.getEntryType().name())
                .build();
    }

    public List<EntryResponseDto> mapEntryEntitiesToResponses
            (List<SupplyEntryEntity> entities) {
        List<EntryResponseDto> responses = new ArrayList<>();
        for (SupplyEntryEntity sE : entities){
            responses.add(this.mapEntryEntityToResponse(sE));
        }
        return responses;
    }

    public SupplyEntryEntity mapEntryRequestToEntity(EntryRequestDto requestDto, SupplyEntity supplyEntity) {
        return SupplyEntryEntity.builder()
                .merenderoId(requestDto.getMerenderoId())
                .supply(supplyEntity)
                .quantity(requestDto.getQuantity())
                .entryType(EntryType.valueOf(requestDto.getEntryType()))
                .build();
    }

    public SupplyLoteEntity mapToLoteEntity(SupplyEntryEntity entryEntity, EntryRequestDto requestDto,
                                            SupplyEntity supplyEntity) {
        return SupplyLoteEntity.builder()
                .merenderoId(requestDto.getMerenderoId())
                .supply(supplyEntity)
                .entry(entryEntity)
                .initialQuantity(requestDto.getQuantity())
                .currentQuantity(requestDto.getQuantity())
                .expirationDate(requestDto.getExpirationDate())
                .notified(false)
                .build();
    }
}
