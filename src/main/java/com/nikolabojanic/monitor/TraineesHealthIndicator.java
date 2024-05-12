package com.nikolabojanic.monitor;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Profile("local")
@Component
public class TraineesHealthIndicator implements HealthIndicator {
    private final JdbcTemplate jdbcTemplate;

    public TraineesHealthIndicator(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Health health() {
        try {
            jdbcTemplate.queryForList("SELECT * FROM trainee", List.class);
            return Health.up().withDetail("trainees", "Is available").build();
        } catch (DataAccessException e) {
            return Health.down().withDetail("trainees", "Is not available: " + e.getMessage()).build();
        }
    }
}
