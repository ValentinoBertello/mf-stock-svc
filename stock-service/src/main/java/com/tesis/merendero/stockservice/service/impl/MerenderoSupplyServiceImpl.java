package com.tesis.merendero.stockservice.service.impl;

import com.tesis.merendero.stockservice.clients.MerenderoRestTemplate;
import com.tesis.merendero.stockservice.dto.SupplyIdsDto;
import com.tesis.merendero.stockservice.dto.apiExterna.MerenderoResponse;
import com.tesis.merendero.stockservice.dto.requests.SupplyRequestDto;
import com.tesis.merendero.stockservice.dto.responses.SupplyResponseDto;
import com.tesis.merendero.stockservice.entities.MerenderoSupplyEntity;
import com.tesis.merendero.stockservice.entities.SupplyEntity;
import com.tesis.merendero.stockservice.entities.SupplyStockEntity;
import com.tesis.merendero.stockservice.mapper.SupplyDataMapper;
import com.tesis.merendero.stockservice.repository.MerenderoSupplyRepository;
import com.tesis.merendero.stockservice.repository.SupplyLoteRepository;
import com.tesis.merendero.stockservice.repository.SupplyRepository;
import com.tesis.merendero.stockservice.service.MerenderoSupplyService;
import com.tesis.merendero.stockservice.service.SupplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MerenderoSupplyServiceImpl implements MerenderoSupplyService {

    private final MerenderoSupplyRepository merenderoSupplyRepository;
    private final SupplyRepository supplyRepository;
    private final SupplyLoteRepository loteRepository;
    private final SupplyService supplyService;
    private final MerenderoRestTemplate merenderoRestTemplate;


    public MerenderoSupplyServiceImpl(MerenderoSupplyRepository merenderoSupplyRepository, SupplyRepository supplyRepository, SupplyLoteRepository loteRepository, MerenderoRestTemplate merenderoRestTemplate, SupplyDataMapper supplyDataMapper, SupplyService supplyService) {
        this.merenderoSupplyRepository = merenderoSupplyRepository;
        this.supplyRepository = supplyRepository;
        this.loteRepository = loteRepository;
        this.merenderoRestTemplate = merenderoRestTemplate;
        this.supplyService = supplyService;
    }


    @Override
    @Transactional
    public void addGlobalSupplyToMerendero(Long merenderoId, Long supplyId, String username) throws AccessDeniedException {

        MerenderoResponse merenderoResponse = new MerenderoResponse();

        // 1. Validar que el supply existe y es global
        SupplyEntity supply = supplyRepository.findById(supplyId)
                .orElseThrow(() -> new RuntimeException("Supply no encontrado"));

        if (supply.getMerenderoId() != null) {
            throw new RuntimeException("Solo se pueden asociar supplies globales");
        }

        // 2. Validar que el merendero existe (llamada HTTP a otro microservicio)
        try {
            merenderoResponse = merenderoRestTemplate.getMerenderoById(merenderoId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Merendero no existe");
        } catch (RestClientException e) {
            throw new RuntimeException("Error al validar el merendero: " + e.getMessage());
        }

        // 3. Validar que la persona que esta autenticada sea la dueña de dicho merendero
        if (!merenderoResponse.getManagerEmail().equals(username)) {
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");
        }

        // 4. Validar que no exista la relación merendero-supply (NUEVA VALIDACIÓN)
        if (merenderoSupplyRepository.existsById_MerenderoIdAndId_SupplyId(merenderoId, supplyId)) {
            throw new RuntimeException("El supply ya está asociado a este merendero");
        }

        // 5. Guardar la relación
        MerenderoSupplyEntity relation = MerenderoSupplyEntity.builder()
                .id(MerenderoSupplyEntity.MerenderoSupplyId.builder()
                        .merenderoId(merenderoId)
                        .supplyId(supplyId)
                        .build())
                .build();

        this.merenderoSupplyRepository.save(relation);
    }

    @Override
    @Transactional
    public SupplyIdsDto addGlobalSuppliesToMerendero(Long merenderoId, SupplyIdsDto supplyIdsDto, String name) throws AccessDeniedException {
        MerenderoResponse merenderoResponse = new MerenderoResponse();

        // 1. Validar que cada supply existe y es global
        for (Long id : supplyIdsDto.getIds()) {
            SupplyEntity supply = supplyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Supply no encontrado"));
            if (supply.getMerenderoId() != null) {
                throw new RuntimeException("Solo se pueden asociar supplies globales");
            }
        }

        // 2. Validar que el merendero existe (llamada HTTP a otro microservicio)
        try {
            merenderoResponse = merenderoRestTemplate.getMerenderoById(merenderoId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Merendero no existe");
        } catch (RestClientException e) {
            throw new RuntimeException("Error al validar el merendero: " + e.getMessage());
        }

        // 3. Validar que la persona que esta autenticada sea la dueña de dicho merendero
        if (!merenderoResponse.getManagerEmail().equals(name)) {
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");
        }

        // 4. Validar que no exista la relación merendero-supply
        for (Long id : supplyIdsDto.getIds()) {
            if (merenderoSupplyRepository.existsById_MerenderoIdAndId_SupplyId(merenderoId, id)) {
                throw new RuntimeException("El supply ya está asociado a este merendero");
            }
        }

        List<Long> longList = new ArrayList<>();
        // 5. Guardar la relación
        for (Long id : supplyIdsDto.getIds()) {
            MerenderoSupplyEntity relation = MerenderoSupplyEntity.builder()
                    .id(MerenderoSupplyEntity.MerenderoSupplyId.builder()
                            .merenderoId(merenderoId)
                            .supplyId(id)
                            .build())
                    .build();
            this.merenderoSupplyRepository.save(relation);
            longList.add(id);
        }
        return SupplyIdsDto.builder().ids(longList).build();
    }

    @Override
    @Transactional
    public SupplyIdsDto addOwnSupplyToMerendero(Long merenderoId, SupplyRequestDto supplyRequestDto, String name) throws AccessDeniedException {
        MerenderoResponse merenderoResponse = new MerenderoResponse();

        // 1. Validar que el merendero existe (llamada HTTP a otro microservicio)
        try {
            merenderoResponse = merenderoRestTemplate.getMerenderoById(merenderoId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Merendero no existe");
        } catch (RestClientException e) {
            throw new RuntimeException("Error al validar el merendero: " + e.getMessage());
        }

        // 2. Validar que la persona que esta autenticada sea la dueña de dicho merendero
        if (!merenderoResponse.getManagerEmail().equals(name)) {
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");
        }

        // 3. Guardar el nuevo supply personalizado
        SupplyResponseDto responseDto = this.supplyService.saveSupplyPersonalized(supplyRequestDto,
                supplyRequestDto.getSupplyCategoryId());

        // 4. Guardar la relación
            MerenderoSupplyEntity relation = MerenderoSupplyEntity.builder()
                    .id(MerenderoSupplyEntity.MerenderoSupplyId.builder()
                            .merenderoId(merenderoId)
                            .supplyId(responseDto.getId())
                            .build())
                    .build();
            this.merenderoSupplyRepository.save(relation);
        List<Long> longList = new ArrayList<>();
        longList.add(responseDto.getId());
        return SupplyIdsDto.builder().ids(longList).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getSuppliesByMerenderoId(Long merenderoId) {
        return this.merenderoSupplyRepository.findSupplyIdsByMerenderoId(merenderoId);
    }


    @Override
    @Transactional
    public Long removeSupplyFromMerendero(Long merenderoId, Long supplyId, String username) throws AccessDeniedException {

        // Validar que la persona que esta autenticada sea la dueña de dicho merendero
        this.validateOwnOfMerendero(merenderoId, username);

        // Validar que el merendero tenga ese supply
        if(!this.merenderoSupplyRepository.existsById_MerenderoIdAndId_SupplyId(merenderoId, supplyId)) {
            throw new RuntimeException("Relación no encontrada");
        }

        // Marcar el supply como inactivo
        SupplyEntity supplyEntity = this.supplyRepository.findById(supplyId).get();
        supplyEntity.setActive(false);
        this.supplyRepository.save(supplyEntity);

        this.loteRepository.deleteAllBySupplyId(supplyId);
        this.merenderoSupplyRepository.deleteById(new MerenderoSupplyEntity.MerenderoSupplyId(merenderoId, supplyId));
        return supplyId;
    }

    // Validar que la persona que esta autenticada sea la dueña de dicho merendero
    private MerenderoResponse validateOwnOfMerendero(Long merenderoId, String username) throws AccessDeniedException {
        MerenderoResponse merenderoResponse = merenderoRestTemplate.getMerenderoById(merenderoId);
        if (!merenderoResponse.getManagerEmail().equals(username)) {
            throw new AccessDeniedException("No tienes permiso para realizar esta acción");
        }
        return merenderoResponse;
    }

}
