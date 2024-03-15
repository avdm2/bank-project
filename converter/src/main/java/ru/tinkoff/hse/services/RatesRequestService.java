package ru.tinkoff.hse.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.hse.models.RatesResponse;

@Service
public class RatesRequestService {

    private final KeycloakTokenRequestService keycloakTokenRequestService;

    @Value("${app.rates-url}")
    private String ratesUrl;

    public RatesRequestService(KeycloakTokenRequestService keycloakTokenRequestService) {
        this.keycloakTokenRequestService = keycloakTokenRequestService;
    }

    public RatesResponse getRatesFromRequest() {
        String token = keycloakTokenRequestService.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<RatesResponse> response = new RestTemplate()
                .getForEntity("http://rates:8080/rates",
                        RatesResponse.class,
                        new HttpEntity<>(null, headers));
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("Rates is unavailable", ratesUrl + "/rates");
        }

        return response.getBody();
    }
}
