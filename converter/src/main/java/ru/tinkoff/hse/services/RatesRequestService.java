package ru.tinkoff.hse.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.entities.RatesResponse;

import java.time.Duration;

@Service
public class RatesRequestService {

    @Value("${app.rates-url}")
    private String ratesUrl;

    public RatesResponse getRatesFromRequest() throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build()
                .getForEntity(ratesUrl, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("Rates is unavailable", "http://rates:8080/rates");
        }

        return new ObjectMapper().readValue(response.getBody(), RatesResponse.class);
    }
}
