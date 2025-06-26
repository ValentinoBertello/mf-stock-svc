package com.tesis.merendero.stockservice.clients;

import com.tesis.merendero.stockservice.dto.apiExterna.MerenderoResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class MerenderoRestTemplate {

    private final RestTemplate restTemplate;

    public MerenderoRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    String baseUrl = "http://localhost:8081/";

    public MerenderoResponse getMerenderoById(Long id){
        return Objects.requireNonNull
                (this.restTemplate.getForEntity(this.baseUrl + "merenderos/byId/" + id,
                        MerenderoResponse.class).getBody());
    }

}
