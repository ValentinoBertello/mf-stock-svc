package com.tesis.merendero.stockservice.controllers;

import com.tesis.merendero.stockservice.dto.responses.StockItemDto;
import com.tesis.merendero.stockservice.dto.responses.StockLoteDto;
import com.tesis.merendero.stockservice.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    //todo validar lo del dueño del merendero y eso
    @GetMapping("/from-merendero/{merenderoId}")
    public ResponseEntity<List<StockItemDto>> getStockInventory(@PathVariable Long merenderoId) {
        return ResponseEntity.ok(this.stockService.getStockInventory(merenderoId));
    }

    @GetMapping("/from-merendero/by-supply/{merenderoId}/{supplyId}")
    public ResponseEntity<StockItemDto> getStockInventoryBySupply(@PathVariable Long merenderoId,
                                                                @PathVariable Long supplyId) {
        return ResponseEntity.ok(this.stockService.getStockInventoryBySupply(merenderoId, supplyId));
    }

    @GetMapping("/lotes/merendero/{merenderoId}/supply/{supplyId}")
    public ResponseEntity<List<StockLoteDto>> getLotesBySupply(@PathVariable Long merenderoId,
                                                               @PathVariable Long supplyId) {
        return ResponseEntity.ok(this.stockService.getLotesBySupply(merenderoId, supplyId));
    }
}
