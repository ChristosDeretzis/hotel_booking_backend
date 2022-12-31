package com.deretzis.hotel_booking_backend.functional

import com.deretzis.hotel_booking_backend.HotelBookingBackendApplication
import com.deretzis.hotel_booking_backend.functional.requests.WebRequests
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.PostConstruct
import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [HotelBookingBackendApplication])
@AutoConfigureWebTestClient(timeout = "30000")
@Testcontainers
@ActiveProfiles("test")
abstract class BaseFunctionalSpec extends Specification{

    @Autowired
    WebTestClient webTestClient

    @Autowired
    WebRequests webRequests

    static DockerComposeContainer dockerComposeContainer

    static  {
        dockerComposeContainer = new DockerComposeContainer(new File("docker-compose-test.yml"))
                .withExposedService("postgres", 5432, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(120)))
                .withExposedService("keycloak", 8080,Wait.forHttp("/auth").withStartupTimeout(Duration.ofSeconds(120)))
                .withLocalCompose(true)
                .withOptions("--compatibility")
        dockerComposeContainer.start()
    }


    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", () ->
                "jdbc:postgresql://" +
                        dockerComposeContainer.getServiceHost("postgres_1", 5432) +
                        ":" +
                        dockerComposeContainer.getServicePort("postgres_1", 5432) +
                        "/" + "booking_service_db");

        registry.add("keycloak.auth-server-url", () ->
                "http://" +
                        dockerComposeContainer.getServiceHost("keycloak_1", 8080) +
                        ":" +
                        dockerComposeContainer.getServicePort("keycloak_1", 8080) +
                        "/" + "auth");

    }

    @PostConstruct
    def postSetup() {
        webRequests.setWebTestClient(webTestClient)
    }
}
