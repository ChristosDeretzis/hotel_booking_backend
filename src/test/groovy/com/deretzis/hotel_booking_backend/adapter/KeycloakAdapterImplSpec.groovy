package com.deretzis.hotel_booking_backend.adapter

import com.deretzis.hotel_booking_backend.configuration.SecurityConfiguration
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

@ContextConfiguration(classes = [KeycloakAdapterImpl, SecurityConfiguration])
@AutoConfigureWireMock(port = 8070)
class KeycloakAdapterImplSpec extends Specification {

    @Shared
    String baseUrl = "http://localhost:8070/auth"
    String realm = "hotel_booking_service"

    def setup() {

    }
}
