package com.innowise.authservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.innowise.authservice.config.JwtProperties;
import com.innowise.authservice.dto.ValidateResponse;
import com.innowise.authservice.entity.Credential;
import com.innowise.authservice.entity.enums.Role;
import com.innowise.authservice.service.implementation.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {
  private JwtService service;
  private Credential credential;

  @BeforeEach
  void setup() {
    JwtProperties properties = new JwtProperties();
    properties.setSecret("abcdefghijklmnopqrstuvwxyzabcdefghijklmnop");
    properties.setAccessTokenExpiration(60000);
    properties.setRefreshTokenExpiration(120000);

    service = new JwtServiceImpl(properties);

    credential = new Credential();
    credential.setCredentialId(1L);
    credential.setUserId(10L);
    credential.setLogin("lefreshz");
    credential.setRole(Role.ROLE_ADMIN);
  }

  @Test
  void shouldGenerateAndValidateAccessToken() {
    String token = service.generateAccessToken(credential);

    ValidateResponse response = service.validateToken(token);

    assertTrue(response.isValid());
    assertEquals(10L, response.getUserId());
    assertEquals(Role.ROLE_ADMIN, response.getRole());
  }

  @Test
  void shouldGenerateAndValidateRefreshToken() {
    String token = service.generateRefreshToken(credential);

    assertTrue(service.isRefreshToken(token));
  }

  @Test
  void shouldExtractLogin() {
    String token = service.generateAccessToken(credential);

    String login = service.extractLogin(token);

    assertEquals("lefreshz", login);
  }

  @Test
  void shouldReturnInvalidForBrokenToken() {
    ValidateResponse response = service.validateToken("invalid-token");

    assertFalse(response.isValid());
  }
}
