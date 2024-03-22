package ru.tinkoff.hse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
            value = { HttpClientErrorException.class },
            maxAttempts = 4,
            backoff = @Backoff(delayExpression = "50", multiplier = 2)
    )
    public RatesResponse getRatesFromRequest() {
        String token = keycloakTokenRequestService.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<RatesResponse> response = new RestTemplate()
                .getForEntity("http://rates:8080/rates",
                        RatesResponse.class,
                        new HttpEntity<>(null, headers));
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.warn("Rates is unavailable");
        }

        return response.getBody();
    }

    @Recover
    public RatesResponse recover(HttpClientErrorException e) {
        throw new RatesRequestException("failed to get rates after retries");
    }
}
