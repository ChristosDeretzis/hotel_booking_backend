package com.service.hotel.booking.adapter;

import com.service.hotel.booking.error.AuthErrorCodes;
import com.service.hotel.booking.error.AuthErrorMessage;
import com.service.hotel.booking.exception.GenericKeycloakException;
import com.service.hotel.booking.exception.RequestKeycloakException;
import com.service.hotel.booking.exception.UnauthorizedKeycloakException;
import com.service.hotel.booking.exception.UserExistsKeycloakException;
import com.service.hotel.booking.keycloak.KeycloakCreateUserRequestDto;
import com.service.hotel.booking.keycloak.ServiceAccessTokenResponseDto;
import com.service.hotel.booking.keycloak.UserRepresentation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KeycloakAdapterImpl implements KeycloakAdapter {
    private static final String SERVICE_TOKEN_ENDPOINT = "/realms/%s/protocol/openid-connect/token";
    private static final String USER_ENDPOINT = "/admin/realms/%s/users";

    public static final String EMAIL_QUERY_PARAMETER = "email";

    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_SECRET_KEY = "client_secret";
    private static final String GRANT_TYPE_KEY = "grant_type";

    private final String realm;
    private final String clientId;
    private final String clientSecret;
    private final String grantType;

    private final WebClient webClient;


    @Autowired
    public KeycloakAdapterImpl(
            @Qualifier("keycloakWebClient") WebClient webClient,
            @Value("${my_keycloak.realm}") String realm,
            @Value("${my_keycloak.client_id}") String clientId,
            @Value("${my_keycloak.client_secret}") String clientSecret,
            @Value("${my_keycloak.grant_type}") String grantTtype) {
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantTtype;
        this.webClient = webClient;

    }

    private ServiceAccessTokenResponseDto obtainServiceAccessToken() {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(CLIENT_ID_KEY, clientId);
        requestBody.add(CLIENT_SECRET_KEY, clientSecret);
        requestBody.add(GRANT_TYPE_KEY, grantType);

        Mono<ServiceAccessTokenResponseDto> serviceAccessTokenMono = webClient
                .post()
                .uri(String.format(SERVICE_TOKEN_ENDPOINT, realm))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals,
                        r -> {
                            AuthErrorMessage authErrorMessage = new AuthErrorMessage(
                                    AuthErrorCodes.GENERIC_ERROR.getCode(),
                                    AuthErrorCodes.GENERIC_ERROR.getMessage()
                            );
                            throw new GenericKeycloakException(authErrorMessage);
                        })
                .onStatus(HttpStatus.BAD_REQUEST::equals,
                        r -> {
                            AuthErrorMessage authErrorMessage = new AuthErrorMessage(
                                    AuthErrorCodes.GENERIC_ERROR.getCode(),
                                    AuthErrorCodes.GENERIC_ERROR.getMessage()
                            );
                            throw new GenericKeycloakException(authErrorMessage);
                        })
                .bodyToMono(ServiceAccessTokenResponseDto.class);

        return serviceAccessTokenMono.block();
    }

    public String createKeycloakUser(KeycloakCreateUserRequestDto keycloakCreateUserRequestDto) {
        ServiceAccessTokenResponseDto serviceAccessTokenResponseDto = obtainServiceAccessToken();

        webClient
                .post()
                .uri(String.format(USER_ENDPOINT, realm))
                .headers(h -> h.setBearerAuth(serviceAccessTokenResponseDto.getAccessToken()))
                .headers(h -> h.setContentType(MediaType.APPLICATION_JSON))
                .body(Mono.just(keycloakCreateUserRequestDto), KeycloakCreateUserRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals,
                        r -> {
                            AuthErrorMessage authErrorMessage = new AuthErrorMessage(
                                    AuthErrorCodes.INVALID_ACCESS_TOKEN_ERROR.getCode(),
                                    AuthErrorCodes.INVALID_ACCESS_TOKEN_ERROR.getMessage()
                            );
                            throw new UnauthorizedKeycloakException(authErrorMessage);
                        })
                .onStatus(HttpStatus.BAD_REQUEST::equals,
                        r -> {
                            AuthErrorMessage authErrorMessage = new AuthErrorMessage(
                                    AuthErrorCodes.REQUEST_ERROR.getCode(),
                                    AuthErrorCodes.REQUEST_ERROR.getMessage()
                            );
                            throw new RequestKeycloakException(authErrorMessage);
                        })
                .onStatus(HttpStatus.CONFLICT::equals,
                        r -> {
                            AuthErrorMessage authErrorMessage = new AuthErrorMessage(
                                    AuthErrorCodes.USER_EXISTS_ERROR.getCode(),
                                    AuthErrorCodes.USER_EXISTS_ERROR.getMessage()
                            );
                            throw new UserExistsKeycloakException(authErrorMessage);
                        })
                .bodyToMono(String.class)
                .doOnError(e -> System.out.println(e.toString()))
                .block();

        String keycloakId = getKeycloakUserByEmail(keycloakCreateUserRequestDto.getEmail()).getId();

        return keycloakId;
    }

    private UserRepresentation getKeycloakUserByEmail(String email) {
        ServiceAccessTokenResponseDto serviceAccessTokenResponseDto = obtainServiceAccessToken();

        Mono<List<UserRepresentation>> getUsersResponse = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(String.format(USER_ENDPOINT, realm)).queryParam(EMAIL_QUERY_PARAMETER, email).build())
                .headers(h -> h.setBearerAuth(serviceAccessTokenResponseDto.getAccessToken()))
                .headers(h -> h.setContentType(MediaType.APPLICATION_JSON))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        List<UserRepresentation> users = getUsersResponse.block();
        return users.get(0);
    }
}
