package net.ubn.td.package_web.service;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import net.ubn.td.package_web.config.PackagerProperties;

/**
 * Calls external REST API using OAuth2 client credentials.
 */
@Service
public class RemoteApiService {

    private final WebClient webClient;
    private final PackagerProperties properties;

    public RemoteApiService(WebClient.Builder builder, PackagerProperties properties) {
        this.webClient = builder.build();
        this.properties = properties;
    }

    private String obtainAccessToken() {
        return webClient.post()
                .uri(properties.getTokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .headers(h -> h.setBasicAuth(properties.getClientId(), properties.getClientSecret()))
                .retrieve()
                .bodyToMono(Map.class)
                .map(m -> (String) m.get("access_token"))
                .block();
    }

    public void sendMetadata(Map<String, Object> metadata) {
        String token = obtainAccessToken();
        webClient.post()
                .uri(properties.getRemoteApiUrl())
                .bodyValue(metadata)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(ex -> {
                    // TODO handle errors
                    return Mono.empty();
                })
                .block();
    }
}
