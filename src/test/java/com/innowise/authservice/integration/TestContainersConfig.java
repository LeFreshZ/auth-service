package com.innowise.authservice.integration;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainersConfig {

  public static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:16-alpine")
          .withDatabaseName("auth_db")
          .withUsername("user")
          .withPassword("password");

  static {
    POSTGRES.start();
  }
}
