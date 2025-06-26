package com.tesis.merendero.stockservice.service;

import com.tesis.merendero.stockservice.dto.requests.EntryRequestDto;
import com.tesis.merendero.stockservice.dto.requests.EntryWithExpenseRequest;
import com.tesis.merendero.stockservice.dto.responses.EntryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EntryService {
    EntryResponseDto saveEntry(Long merenderoId, EntryWithExpenseRequest entryRequestDto, String name);

    List<EntryResponseDto> getAllEntries(Long merenderoId, String name);
}
