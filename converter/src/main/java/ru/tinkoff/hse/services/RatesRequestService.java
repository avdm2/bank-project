package ru.tinkoff.hse.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.entities.RatesResponse;

import java.time.Duration;

@Service
public class RatesRequestService {

    public RatesResponse getRatesFromRequest() throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build()
                .getForEntity("http://rates:8080/rates", String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("Rates is unavailable", "http://rates:8080/rates");
        }

        return new ObjectMapper().readValue(response.getBody(), RatesResponse.class);
    }
}
