package com.tesis.merendero.stockservice.service;

import com.tesis.merendero.stockservice.dto.responses.StockItemDto;
import com.tesis.merendero.stockservice.dto.responses.StockLoteDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface StockService {

    List<StockItemDto> getStockInventory(Long merenderoId);

    List<StockLoteDto> getLotesBySupply(Long merenderoId, Long supplyId);

    StockItemDto getStockInventoryBySupply(Long merenderoId, Long supplyId);
}
