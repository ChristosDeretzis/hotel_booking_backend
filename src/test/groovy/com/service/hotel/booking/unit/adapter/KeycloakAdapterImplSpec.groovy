package com.service.hotel.booking.unit.adapter

import com.service.hotel.booking.adapter.KeycloakAdapter
import com.service.hotel.booking.configuration.KeycloakConfiguration
import com.service.hotel.booking.exception.RequestKeycloakException
import com.service.hotel.booking.exception.UnauthorizedKeycloakException
import com.service.hotel.booking.exception.UserExistsKeycloakException
import com.service.hotel.booking.keycloak.KeycloakCreateUserRequestDto
import com.service.hotel.booking.keycloak.KeycloakPasswordCredentials
import com.service.hotel.booking.keycloak.ServiceAccessTokenResponseDto
import com.service.hotel.booking.keycloak.UserRepresentation
import com.fasterxml.jackson.databind.ObjectMapper
import com.service.hotel.booking.adapter.KeycloakAdapterImpl
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

@ContextConfiguration(classes = [KeycloakAdapterImpl, KeycloakConfiguration])
@AutoConfigureWireMock(port = 8070)
class KeycloakAdapterImplSpec extends Specification {

    @Shared
    String baseUrl = "http://localhost:8070/auth"
    @Shared
    String realm = "hotel_booking_service"
    @Shared
    String clientId = "hotel_booking_backend"
    @Shared
    String clientSecret = "YSwfd956q5TKze8aCsCiR23joPxU1TIo"
    @Shared
    String grantType = "client_credentials"
    @Shared
    ServiceAccessTokenResponseDto serviceAccessTokenResponseDto = new ServiceAccessTokenResponseDto(accessToken: "access token")

    KeycloakAdapter keycloakAdapter
    WebClient webClient

    def setup() {
        resetToDefault()

        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build()

        keycloakAdapter = new KeycloakAdapterImpl(webClient, realm, clientId, clientSecret, grantType)

        stubFor(post("/auth/realms/hotel_booking_service/protocol/openid-connect/token")
                .willReturn(aResponse().withBody(new ObjectMapper().writeValueAsString(serviceAccessTokenResponseDto))
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                ))
    }

    def "Create a Keycloak User"() {
        given:
            KeycloakCreateUserRequestDto keycloakCreateUserRequestDto = new KeycloakCreateUserRequestDto(
                    firstName: "John",
                    lastName: "Dimou",
                    email: "john.dimou@email.com",
                    username: "john_dimou",
                    enabled: true,
                    credentials: [new KeycloakPasswordCredentials(type: "password", value: "user1234", temporary: false)]
            )

            UserRepresentation userRepresentation = new UserRepresentation(
                    "user_new", "01012023172304", "john_dimou", true, false, "John", "Dimou", "john.dimou@email.com"
            )
            List<UserRepresentation> userRepresentationList = []
            userRepresentationList.add(userRepresentation)

            stubFor(post("/auth/admin/realms/hotel_booking_service/users")
                    .withHeader("Authorization", containing("Bearer ${serviceAccessTokenResponseDto.accessToken}"))
                    .willReturn(aResponse().withBody(new ObjectMapper().writeValueAsString(keycloakCreateUserRequestDto))
                                .withHeader("Content-Type", "application/json")
                                .withStatus(201))
                    )

            stubFor(get(String.format("/auth/admin/realms/hotel_booking_service/users?email=%s", keycloakCreateUserRequestDto.getEmail()))
                    .withQueryParam("email", equalTo(keycloakCreateUserRequestDto.getEmail()))
                    .withHeader("Authorization", containing("Bearer ${serviceAccessTokenResponseDto.accessToken}"))
                    .willReturn(aResponse().withBody(new ObjectMapper().writeValueAsString(userRepresentationList))
                            .withHeader("Content-Type", "application/json")
                            .withStatus(200))
            )

        when:
            keycloakAdapter.createKeycloakUser(keycloakCreateUserRequestDto)

        then:
            verify(1, postRequestedFor(urlEqualTo("/auth/admin/realms/hotel_booking_service/users")))
            verify(1, getRequestedFor(urlEqualTo("/auth/admin/realms/hotel_booking_service/users?email=john.dimou@email.com")))
    }

    def "Create a Keycloak User Error"() {
        given:
            KeycloakCreateUserRequestDto keycloakCreateUserRequestDto = new KeycloakCreateUserRequestDto(
                    firstName: "John",
                    lastName: "Dimou",
                    email: "john.dimou@email.com",
                    username: "john_dimou",
                    enabled: true,
                    credentials: [new KeycloakPasswordCredentials(type: "password", value: "user1234", temporary: false)]
            )

            stubFor(post("/auth/admin/realms/hotel_booking_service/users")
                    .withHeader("Authorization", containing("Bearer ${serviceAccessTokenResponseDto.accessToken}"))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withStatus(errorStatus))
            )

        when:
            keycloakAdapter.createKeycloakUser(keycloakCreateUserRequestDto)

        then:
            thrown exception

        where:
            errorStatus                     | exception
            HttpStatus.BAD_REQUEST.value()  | RequestKeycloakException
            HttpStatus.UNAUTHORIZED.value() | UnauthorizedKeycloakException
            HttpStatus.CONFLICT.value()     | UserExistsKeycloakException
    }
}
