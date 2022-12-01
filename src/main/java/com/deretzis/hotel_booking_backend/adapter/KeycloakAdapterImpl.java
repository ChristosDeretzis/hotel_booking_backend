package com.deretzis.hotel_booking_backend.adapter;

import com.deretzis.hotel_booking_backend.keycloak.KeycloakCreateUserRequestDto;
import com.deretzis.hotel_booking_backend.keycloak.ServiceAccessTokenResponseDto;
import com.deretzis.hotel_booking_backend.keycloak.UserRepresentation;
import com.deretzis.hotel_booking_backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class KeycloakAdapterImpl implements KeycloakAdapter {

    private static final String BASE_URL = "http://localhost:8000/auth";
    private static final String SERVICE_TOKEN_ENDPOINT = "/realms/%s/protocol/openid-connect/token";
    private static final String USER_ENDPOINT = "/admin/realms/%s/users";

    public static final String EMAIL_QUERY_PARAMETER = "email";

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

    public String createKeycloakUser(KeycloakCreateUserRequestDto KeycloakCreateUserRequestDto) {
        ServiceAccessTokenResponseDto serviceAccessTokenResponseDto = obtainServiceAccessToken();

        webClient
                .post()
                .uri(String.format(USER_ENDPOINT, realm))
                .headers(h -> h.setBearerAuth(serviceAccessTokenResponseDto.getAccessToken()))
                .headers(h -> h.setContentType(MediaType.APPLICATION_JSON))
                .body(Mono.just(KeycloakCreateUserRequestDto), KeycloakCreateUserRequestDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> System.out.println(e.toString()))
                .block();

        String keycloakId = getKeycloakUserByEmail(KeycloakCreateUserRequestDto.getEmail()).getId();

        return keycloakId;
    }

    public UserRepresentation getKeycloakUserByEmail(String email) {
        ServiceAccessTokenResponseDto serviceAccessTokenResponseDto = obtainServiceAccessToken();

        Mono<List<UserRepresentation>> getUsersResponse = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(String.format(USER_ENDPOINT, realm)).queryParam(EMAIL_QUERY_PARAMETER, email).build())
                .headers(h -> h.setBearerAuth(serviceAccessTokenResponseDto.getAccessToken()))
                .headers(h -> h.setContentType(MediaType.APPLICATION_JSON))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});

        List<UserRepresentation> users = getUsersResponse.block();
        return users.get(0);
    }
}
