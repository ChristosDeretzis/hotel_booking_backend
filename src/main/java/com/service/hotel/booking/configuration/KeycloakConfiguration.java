package com.service.hotel.booking.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KeycloakConfiguration {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Bean
    public WebClient keycloakWebClient() {

        return WebClient.builder()
                .baseUrl(authServerUrl)
                .build();
    }

}
