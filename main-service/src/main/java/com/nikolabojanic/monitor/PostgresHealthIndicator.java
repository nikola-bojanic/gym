package com.nikolabojanic.monitor;

import javax.sql.DataSource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class PostgresHealthIndicator implements HealthIndicator {
    private final JdbcTemplate jdbcTemplate;

    public PostgresHealthIndicator(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Health health() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Health.up().withDetail("psql", "Is healthy").build();
        } catch (DataAccessException e) {
            return Health.down().withDetail("psql", "Is not healthy: " + e.getMessage()).build();
        }
    }
}
