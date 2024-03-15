package ru.tinkoff.hse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.hse.models.RatesResponse;

@Service
@Slf4j
public class RatesRequestService {

    private final KeycloakTokenRequestService keycloakTokenRequestService;

    @Value("${app.rates-url}")
    private String ratesUrl;

    public RatesRequestService(KeycloakTokenRequestService keycloakTokenRequestService) {
        this.keycloakTokenRequestService = keycloakTokenRequestService;
    }

    public RatesResponse getRatesFromRequest() {
        log.info("getting token");
        String token = keycloakTokenRequestService.getToken();
        log.info("token==null: {}", token==null);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<RatesResponse> response = new RestTemplate()
                .getForEntity("http://rates:8080/rates",
                        RatesResponse.class,
                        new HttpEntity<>(null, headers));
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("Rates is unavailable", "http://rates:8080/rates");
        }

        return response.getBody();
    }
}
