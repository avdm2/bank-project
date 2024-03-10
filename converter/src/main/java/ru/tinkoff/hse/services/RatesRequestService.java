package ru.tinkoff.hse.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.entities.RatesResponse;

import java.time.Duration;

@Service
public class RatesRequestService {

    private final KeycloakTokenRequestService keycloakTokenRequestService;

    @Value("${app.rates-url}")
    private String ratesUrl;

    public RatesRequestService(KeycloakTokenRequestService keycloakTokenRequestService) {
        this.keycloakTokenRequestService = keycloakTokenRequestService;
    }

    public RatesResponse getRatesFromRequest() throws JsonProcessingException {
        String token = keycloakTokenRequestService.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<String> response = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build()
                .getForEntity(ratesUrl, String.class, new HttpEntity<>(null, headers));
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("Rates is unavailable", "http://rates:8080/rates");
        }

        return new ObjectMapper().readValue(response.getBody(), RatesResponse.class);
    }
}
