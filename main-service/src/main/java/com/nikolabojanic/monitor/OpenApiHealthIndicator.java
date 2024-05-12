package com.nikolabojanic.monitor;

import java.net.URI;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenApiHealthIndicator implements HealthIndicator {
    private final RestTemplate restTemplate;

    public OpenApiHealthIndicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Health health() {
        try {
            URI openApiEndpoint = new URI("http://localhost:8080/v3/api-docs");
            ResponseEntity<String> response = restTemplate.getForEntity(openApiEndpoint, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Health.up().withDetail("openAPI", "Is available").build();
            } else {
                return Health.down().withDetail("openAPI", "Is not available - HTTP Status: "
                    + response.getStatusCode().value()).build();
            }
        } catch (Exception e) {
            return Health.down().withDetail("openAPI", "Is not available - Exception: "
                + e.getMessage()).build();
        }
    }
}

