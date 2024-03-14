package ru.tinkoff.hse.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.models.KeycloakTokenRequest;
import ru.tinkoff.hse.models.KeycloakTokenResponse;

import java.time.Duration;

@Service
public class KeycloakTokenRequestService {

    @Value("${app.keycloak-url}")
    private String keycloakUrl;

    @Value("${app.keycloak-realm}")
    private String keycloakRealm;

    @Value("${app.client-secret}")
    private String clientSecret;

    @Value("${app.client-id}")
    private String clientId;

    public String getToken() {
        ResponseEntity<KeycloakTokenResponse> response = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build()
                .postForEntity(keycloakUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token",
                        new KeycloakTokenRequest().setClientId(clientId).setClientSecret(clientSecret), KeycloakTokenResponse.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException(
                    "Keycloak is unavailable", "{keycloakUrl}/realms/{keycloakRealm}/protocol/openid-connect/token"
            );
        }
        return response.getBody().getAccessToken();
    }
}
