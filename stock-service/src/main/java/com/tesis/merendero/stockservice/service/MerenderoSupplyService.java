package com.tesis.merendero.stockservice.service;

import com.tesis.merendero.stockservice.dto.SupplyIdsDto;
import com.tesis.merendero.stockservice.dto.requests.SupplyRequestDto;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public interface MerenderoSupplyService {

    void addGlobalSupplyToMerendero(Long merenderoId, Long supplyId, String username) throws AccessDeniedException;

    SupplyIdsDto addGlobalSuppliesToMerendero(Long merenderoId, SupplyIdsDto supplyIdsDto, String name) throws AccessDeniedException;

    SupplyIdsDto addOwnSupplyToMerendero(Long merenderoId, SupplyRequestDto supplyRequestDto, String name) throws AccessDeniedException;


    List<Long> getSuppliesByMerenderoId(java.lang.Long merenderoId);

    Long removeSupplyFromMerendero(Long merenderoId, Long supplyId, String username) throws AccessDeniedException;
}
