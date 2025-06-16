package net.ubn.td.package_web.service;

import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import net.ubn.td.package_web.config.PackagerProperties;

/**
 * Calls external REST API using OAuth2 client credentials.
 */
@Service
public class RemoteApiService {

    private final RestTemplate restTemplate;
    private final PackagerProperties properties;

    public RemoteApiService(RestTemplateBuilder builder, PackagerProperties properties) {
        this.restTemplate = builder.build();
        this.properties = properties;
    }

    private String obtainAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(properties.getClientId(), properties.getClientSecret());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        Map<?, ?> response = restTemplate.postForObject(properties.getTokenUrl(), request, Map.class);
        return response != null ? (String) response.get("access_token") : null;
    }

    public void sendMetadata(Map<String, Object> metadata) {
        String token = obtainAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(metadata, headers);
        try {
            restTemplate.postForObject(properties.getRemoteApiUrl(), request, Void.class);
        } catch (Exception ex) {
            // TODO handle errors
        }
    }
}
