package com.tesis.merendero.stockservice.controllers;

import com.tesis.merendero.stockservice.dto.requests.OutputRequestDto;
import com.tesis.merendero.stockservice.dto.responses.OutputResponseDto;
import com.tesis.merendero.stockservice.service.OutputService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/output")
public class OutputController {

    private final OutputService outputService;

    public OutputController(OutputService outputService) {
        this.outputService = outputService;
    }

    @PostMapping("/save/{merenderoId}")
    public ResponseEntity<OutputResponseDto> saveOutput(
            @PathVariable Long merenderoId,
            @RequestBody OutputRequestDto outputRequestDto,
            Authentication authentication
    ) throws AccessDeniedException, MessagingException {
        return ResponseEntity.ok(outputService.saveOutput(merenderoId, outputRequestDto, authentication.getName()));
    }

    @GetMapping("/all/{merenderoId}")
    public ResponseEntity<List<OutputResponseDto>> getAllOutputs(
            @PathVariable Long merenderoId,
            Authentication authentication
    ) throws AccessDeniedException, MessagingException {
        return ResponseEntity.ok(outputService.getAllOutputs(merenderoId, authentication.getName()));
    }

}
