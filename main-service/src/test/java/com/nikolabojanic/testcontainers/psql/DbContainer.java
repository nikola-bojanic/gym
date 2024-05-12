package com.nikolabojanic.testcontainers.psql;

import org.testcontainers.containers.PostgreSQLContainer;

public class DbContainer extends PostgreSQLContainer<DbContainer> {
    private static final String IMAGE_VERSION = "postgres:16";
    private static DbContainer container;

    private DbContainer() {
        super(IMAGE_VERSION);
    }

    /**
     * Retrieves a created {@link DbContainer}. If a container doesn't exist, creates it.
     *
     * @return Created {@link DbContainer}
     */
    public static DbContainer getContainer() {
        if (container == null) {
            container = new DbContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.datasource.url", this.getJdbcUrl());
        System.setProperty("spring.datasource.username", this.getUsername());
        System.setProperty("spring.datasource.password", this.getPassword());
    }

    @Override
    public void stop() {
    }
}