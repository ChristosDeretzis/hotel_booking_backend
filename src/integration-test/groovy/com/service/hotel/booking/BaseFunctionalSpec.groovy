package com.service.hotel.booking

import com.service.hotel.booking.requests.WebRequests
import dasniko.testcontainers.keycloak.KeycloakContainer
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [HotelBookingBackendApplication])
@AutoConfigureWebTestClient(timeout = "300000")
@Testcontainers
abstract class BaseFunctionalSpec extends Specification {

    @Autowired
    private WebTestClient webTestClient

    @Autowired
    WebRequests webRequests

    @Shared
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.1")
            .withUsername("christos")
            .withPassword("user91234")
            .withFileSystemBind("./docker/postgres/init-sql", "/docker-entrypoint-initdb.d")
            .withDatabaseName("booking_service_db")

    @Shared
    static KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:21.1.0")
            .withRealmImportFile("keycloak/realm.json")
            .withAdminUsername("admin")
            .withAdminPassword("admin")

    void setupSpec() {
        postgreSQLContainer.start()
        keycloakContainer.start()
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add('spring.datasource.url', () -> postgreSQLContainer.getJdbcUrl());
        registry.add('keycloak.auth-server-url', () -> keycloakContainer.getAuthServerUrl());
    }

    @PostConstruct
    def postSetup() {
        webRequests.setWebTestClient(webTestClient)
    }
}
