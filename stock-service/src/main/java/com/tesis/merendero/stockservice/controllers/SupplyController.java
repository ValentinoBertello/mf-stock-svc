package com.tesis.merendero.stockservice.controllers;

import com.tesis.merendero.stockservice.dto.SupplyIdsDto;
import com.tesis.merendero.stockservice.dto.responses.SupplyResponseDto;
import com.tesis.merendero.stockservice.entities.SupplyCategoryEntity;
import com.tesis.merendero.stockservice.service.SupplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/supply")
public class SupplyController {

    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @GetMapping("/globals")
    public ResponseEntity<List<SupplyResponseDto>> getAllGlobalSupplies(){
        return ResponseEntity.ok(this.supplyService.getAllGlobalSupplies());
    }

    @PostMapping("/by-ids")
    public ResponseEntity<List<SupplyResponseDto>> getSuppliesByIds(@RequestBody SupplyIdsDto supplyIdsDto){
        return ResponseEntity.ok(this.supplyService.getSuppliesByIds(supplyIdsDto));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<SupplyCategoryEntity>> getAllSupplyCategories(){
        return ResponseEntity.ok(this.supplyService.getAllSupplyCategories());
    }
}
