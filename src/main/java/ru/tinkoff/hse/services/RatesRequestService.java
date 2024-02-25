package ru.tinkoff.hse.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.hse.models.RatesResponse;

import java.net.ConnectException;

@Service
public class RatesRequestService {

    public RatesResponse getRatesFromRequest() throws ConnectException, JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().getForEntity("/rates", String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ConnectException("Rates is unavailable");
        }

        return new ObjectMapper().readValue(response.getBody(), RatesResponse.class);
    }
}
