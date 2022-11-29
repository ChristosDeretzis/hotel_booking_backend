package com.deretzis.hotel_booking_backend.adapter;

import com.deretzis.hotel_booking_backend.dto.ServiceAccessTokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KeycloakAdapterImpl implements KeycloakAdapter {

    private static final String BASE_URL = "http://localhost:8000/auth";
    private static final String SERVICE_TOKEN_ENDPOINT = "/realms/%s/protocol/openid-connect/token";
    private static final String CREATE_USER_ENDPOINT = "/admin/realms/%s/users";

    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_SECRET_KEY = "client_secret";
    private static final String GRANT_TYPE_KEY = "grant_type";

    private final String realm;
    private final String client_id;
    private final String client_secret;
    private final String grant_type;

    private final WebClient webClient;


    @Autowired
    public KeycloakAdapterImpl(
            @Value("${my_keycloak.realm}") String realm,
            @Value("${my_keycloak.client_id}") String client_id,
            @Value("${my_keycloak.client_secret}") String client_secret,
            @Value("${my_keycloak.grant_type}") String grant_type) {
        this.realm = realm;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.grant_type = grant_type;

        this.webClient = WebClient
                .builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public ServiceAccessTokenResponseDto obtainServiceAccessToken() {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(CLIENT_ID_KEY, client_id);
        requestBody.add(CLIENT_SECRET_KEY, client_secret);
        requestBody.add(GRANT_TYPE_KEY, grant_type);

        Mono<ServiceAccessTokenResponseDto> serviceAccessTokenMono = webClient
                .post()
                .uri(String.format(SERVICE_TOKEN_ENDPOINT, realm))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .bodyToMono(ServiceAccessTokenResponseDto.class);

        return serviceAccessTokenMono.block();
    }

    public void createKeycloakUser() {

    }

}
