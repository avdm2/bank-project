package ru.tinkoff.hse.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.hse.dto.KeycloakTokenResponse;

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

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

        ResponseEntity<KeycloakTokenResponse> response = restTemplate
                .exchange(keycloakUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token",
                        HttpMethod.POST,
                        entity,
                        KeycloakTokenResponse.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new InvalidEndpointRequestException(
                    "Keycloak is unavailable", "{keycloakUrl}/realms/{keycloakRealm}/protocol/openid-connect/token"
            );
        }
        return response.getBody().getAccessToken();
    }
}
