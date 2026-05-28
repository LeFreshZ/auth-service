package com.innowise.authservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.authservice.dto.LoginRequest;
import com.innowise.authservice.dto.RefreshRequest;
import com.innowise.authservice.dto.SaveCredentialsRequest;
import com.innowise.authservice.dto.ValidateRequest;
import com.innowise.authservice.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public abstract class IntegrationTest {

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", TestContainersConfig.POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", TestContainersConfig.POSTGRES::getUsername);
    registry.add("spring.datasource.password", TestContainersConfig.POSTGRES::getPassword);

    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");

    registry.add("jwt.secret", () -> "abcdefghijklmnopqrstuvwxyzabcdefghijklmnop");
    registry.add("jwt.access-token-expiration", () -> 3600000L);
    registry.add("jwt.refresh-token-expiration", () -> 86400000L);
  }

  @Autowired
  protected MockMvc mvc;

  @Autowired
  protected ObjectMapper mapper;

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  @BeforeEach
  void clean() {
    jdbcTemplate.execute("TRUNCATE TABLE credentials RESTART IDENTITY CASCADE");
  }

  protected String createCredentialsRequest(
      String login,
      String password,
      Long userId,
      Role role
  ) throws JsonProcessingException {

    SaveCredentialsRequest request = new SaveCredentialsRequest();

    request.setLogin(login);
    request.setPassword(password);
    request.setUserId(userId);
    request.setRole(role);

    return mapper.writeValueAsString(request);
  }

  protected String createLoginRequest(String login, String password)
      throws JsonProcessingException {
    LoginRequest request = new LoginRequest();

    request.setLogin(login);
    request.setPassword(password);

    return mapper.writeValueAsString(request);
  }

  protected String createRefreshRequest(String token) throws JsonProcessingException {
    RefreshRequest request = new RefreshRequest();

    request.setRefreshToken(token);

    return mapper.writeValueAsString(request);
  }

  protected String createValidateRequest(String token) throws JsonProcessingException {
    ValidateRequest request = new ValidateRequest();

    request.setToken(token);

    return mapper.writeValueAsString(request);
  }
}
