package ru.tinkoff.hse.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.hse.models.RatesResponse;

@Service
public class RatesRequestService {

    public RatesResponse getRatesFromRequest() throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().getForEntity("/rates", String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("Rates is unavailable", "/rates");
        }

        return new ObjectMapper().readValue(response.getBody(), RatesResponse.class);
    }
}
