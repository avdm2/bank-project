package ru.tinkoff.hse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.hse.exceptions.RatesRequestException;
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

    @Retryable(
            value = { InvalidEndpointRequestException.class },
            maxAttempts = 4,
            backoff = @Backoff(delayExpression = "50", multiplier = 2)
    )
    public RatesResponse getRatesFromRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + keycloakTokenRequestService.getToken());
        ResponseEntity<RatesResponse> response = new RestTemplate()
                .exchange("http://rates:8080/rates",
                        HttpMethod.GET,
                        new HttpEntity<>(null, headers),
                        RatesResponse.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException("Rates is unavailable", ratesUrl + "/rates");
        }

        return response.getBody();
    }

    @Recover
    public RatesResponse recover(HttpClientErrorException e) {
        throw new RatesRequestException("failed to get rates after retries");
    }
}
