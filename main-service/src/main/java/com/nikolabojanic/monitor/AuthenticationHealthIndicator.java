package com.nikolabojanic.monitor;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Profile("local")
@Component
public class AuthenticationHealthIndicator implements HealthIndicator {
    private final RestTemplate restTemplate;

    @Autowired
    public AuthenticationHealthIndicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Health health() {
        try {
            RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.OPTIONS,
                new URI("http://localhost:8080/api/v1/users/auth"));
            ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return Health.up().withDetail("userAuthentication", "Is available").build();
            } else {
                return Health.down().withDetail("userAuthentication", "Is not available - HTTP Status: "
                    + response.getStatusCode().value()).build();
            }
        } catch (Exception e) {
            return Health.down().withDetail("userAuthentication", "Is not available - Exception: " + e.getMessage())
                .build();
        }
    }
}
