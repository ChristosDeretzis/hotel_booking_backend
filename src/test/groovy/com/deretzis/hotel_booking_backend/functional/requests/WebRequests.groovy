package com.deretzis.hotel_booking_backend.functional.requests

import com.deretzis.hotel_booking_backend.dto.CreateUserDto
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient

@Component
class WebRequests {

    WebTestClient webTestClient

    def setWebTestClient(WebTestClient webTestClient) {
        this.webTestClient = webTestClient
    }

    def signupUser(CreateUserDto createUserDto) {
        webTestClient.post()
                .uri("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createUserDto)
                .exchange()
    }
}
