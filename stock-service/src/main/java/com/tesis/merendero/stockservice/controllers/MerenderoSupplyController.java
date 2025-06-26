package com.tesis.merendero.stockservice.controllers;

import com.tesis.merendero.stockservice.dto.SupplyIdsDto;
import com.tesis.merendero.stockservice.dto.requests.SupplyRequestDto;
import com.tesis.merendero.stockservice.service.MerenderoSupplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/merendero-supply")
public class MerenderoSupplyController {

    private final MerenderoSupplyService merenderoSupplyService;

    public MerenderoSupplyController(MerenderoSupplyService merenderoSupplyService) {
        this.merenderoSupplyService = merenderoSupplyService;
    }

    @PostMapping("/add-supply/{merenderoId}/supplies/{supplyId}")
    public ResponseEntity<Void> addSupplyToMerendero(
            @PathVariable Long merenderoId,
            @PathVariable Long supplyId,
            Authentication authentication
    ) throws AccessDeniedException {
        merenderoSupplyService.addGlobalSupplyToMerendero(merenderoId, supplyId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    //todo manejar mejor si hay un error
    @PostMapping("/add-supplies/{merenderoId}")
    public ResponseEntity<SupplyIdsDto> addSuppliesToMerendero(
            @PathVariable Long merenderoId,
            @RequestBody SupplyIdsDto supplyIdsDto,
            Authentication authentication
    ) throws AccessDeniedException {
        return ResponseEntity.ok
                (merenderoSupplyService.addGlobalSuppliesToMerendero(merenderoId, supplyIdsDto, authentication.getName()));
    }

    @PostMapping("/add-own-supply/{merenderoId}")
    public ResponseEntity<SupplyIdsDto> addOwnSupplyToMerendero(
            @PathVariable Long merenderoId,
            @RequestBody SupplyRequestDto supplyRequestDto,
            Authentication authentication
    ) throws AccessDeniedException {
        return ResponseEntity.ok
                (merenderoSupplyService.addOwnSupplyToMerendero(merenderoId, supplyRequestDto, authentication.getName()));
    }

    // Obtener supplies asociados a un merendero
    @GetMapping("/merendero/{merenderoId}/supplies")
    public ResponseEntity<List<Long>> getSuppliesByMerenderoId(@PathVariable Long merenderoId) {
        return ResponseEntity.ok(this.merenderoSupplyService.getSuppliesByMerenderoId(merenderoId));
    }

    @DeleteMapping("/delete/merendero/{merenderoId}/supplies/{supplyId}")
    public ResponseEntity<Long> removeSupplyFromMerendero(@PathVariable Long merenderoId,
                                                          @PathVariable Long supplyId,
                                                          Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok(this.merenderoSupplyService.removeSupplyFromMerendero(merenderoId, supplyId,
                                                                                       authentication.getName()));
    }
}
