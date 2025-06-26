package com.tesis.merendero.stockservice.controllers;

import com.tesis.merendero.stockservice.dto.requests.EntryWithExpenseRequest;
import com.tesis.merendero.stockservice.dto.responses.EntryResponseDto;
import com.tesis.merendero.stockservice.service.EntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/entry")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @PostMapping("/save/{merenderoId}")
    public ResponseEntity<EntryResponseDto> saveEntry(
            @PathVariable Long merenderoId,
            @RequestBody EntryWithExpenseRequest entryRequestDto,
            Authentication authentication
    ) throws AccessDeniedException {
        return ResponseEntity.ok(entryService.saveEntry(merenderoId, entryRequestDto, authentication.getName()));
    }

    @GetMapping("/all/{merenderoId}")
    public ResponseEntity<List<EntryResponseDto>> getAllEntries(
            @PathVariable Long merenderoId,
            Authentication authentication
    ) throws AccessDeniedException {
        return ResponseEntity.ok(entryService.getAllEntries(merenderoId, authentication.getName()));
    }
}
