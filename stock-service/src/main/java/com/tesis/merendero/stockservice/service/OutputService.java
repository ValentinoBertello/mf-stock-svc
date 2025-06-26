package com.tesis.merendero.stockservice.service;

import com.tesis.merendero.stockservice.dto.requests.OutputRequestDto;
import com.tesis.merendero.stockservice.dto.responses.OutputResponseDto;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OutputService {
    OutputResponseDto saveOutput(Long merenderoId, OutputRequestDto outputRequestDto, String name) throws MessagingException;

    List<OutputResponseDto> getAllOutputs(Long merenderoId, String name);
}
