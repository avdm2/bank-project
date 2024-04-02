package ru.tinkoff.hse.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.hse.exceptions.RatesRequestException;
import ru.tinkoff.hse.models.RatesResponse;

import java.net.ConnectException;

@Service
public class RatesRequestService {

    private final KeycloakTokenRequestService keycloakTokenRequestService;

    @Value("${app.rates-url}")
    private String ratesUrl;

    public RatesRequestService(KeycloakTokenRequestService keycloakTokenRequestService) {
        this.keycloakTokenRequestService = keycloakTokenRequestService;
    }

    @Retryable(
            retryFor = { HttpClientErrorException.class,
                    RestClientException.class,
                    ConnectException.class,
                    HttpServerErrorException.class },
            maxAttempts = 4,
            backoff = @Backoff(delay = 50, multiplier = 2, maxDelay = 150)
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
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        return response.getBody();
    }

    @Recover
    public RatesResponse recover(Exception e) {
        throw new RatesRequestException("failed to get rates after retries");
    }
}
