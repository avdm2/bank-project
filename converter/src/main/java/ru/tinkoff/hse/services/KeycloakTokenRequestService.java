package ru.tinkoff.hse.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.models.KeycloakTokenResponse;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HashMap<String, String> body = new LinkedHashMap<>();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");

        HttpEntity<HashMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<KeycloakTokenResponse> response = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build()
                .exchange(keycloakUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token",
                        HttpMethod.POST,
                        entity,
                        KeycloakTokenResponse.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new InvalidEndpointRequestException(
                    "Keycloak is unavailable", "{keycloakUrl}/realms/{keycloakRealm}/protocol/openid-connect/token"
            );
        }
        return response.getBody().getAccessToken();
    }
}
