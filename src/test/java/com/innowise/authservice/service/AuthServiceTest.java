package com.innowise.authservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.innowise.authservice.dao.CredentialDao;
import com.innowise.authservice.dto.LoginRequest;
import com.innowise.authservice.dto.RefreshRequest;
import com.innowise.authservice.dto.TokensResponse;
import com.innowise.authservice.dto.ValidateRequest;
import com.innowise.authservice.dto.ValidateResponse;
import com.innowise.authservice.entity.Credential;
import com.innowise.authservice.entity.enums.Role;
import com.innowise.authservice.exception.InvalidCredentialsException;
import com.innowise.authservice.exception.InvalidRefreshTokenException;
import com.innowise.authservice.service.implementation.AuthServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private CredentialDao dao;

  @Mock
  private PasswordEncoder encoder;

  @Mock
  private JwtService jwtService;

  private AuthService authService;

  private Credential credential;

  @BeforeEach
  void setup() {
    authService = new AuthServiceImpl(dao, encoder, jwtService);

    credential = new Credential();
    credential.setCredentialId(1L);
    credential.setUserId(10L);
    credential.setLogin("lefreshz");
    credential.setPassword("encodedPassword");
    credential.setRole(Role.ROLE_ADMIN);
  }

  @Test
  void shouldLoginSuccessfully() {
    LoginRequest request = new LoginRequest();
    request.setLogin("lefreshz");
    request.setPassword("password");

    when(dao.findByLogin("lefreshz")).thenReturn(Optional.of(credential));
    when(encoder.matches("password", "encodedPassword")).thenReturn(true);
    when(jwtService.generateAccessToken(credential)).thenReturn("access-token");
    when(jwtService.generateRefreshToken(credential)).thenReturn("refresh-token");

    TokensResponse response = authService.login(request);

    assertEquals("access-token", response.getAccessToken());
    assertEquals("refresh-token", response.getRefreshToken());
  }

  @Test
  void shouldThrowWhenPasswordInvalid() {
    LoginRequest request = new LoginRequest();
    request.setLogin("lefreshz");
    request.setPassword("wrongPassword");

    when(dao.findByLogin("lefreshz")).thenReturn(Optional.of(credential));
    when(encoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

    assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
  }

  @Test
  void shouldThrowWhenCredentialNotFound() {
    LoginRequest request = new LoginRequest();
    request.setLogin("lefreshz");
    request.setPassword("password");

    when(dao.findByLogin("lefreshz")).thenReturn(Optional.empty());

    assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
  }

  @Test
  void shouldRefreshTokenSuccessfully() {
    RefreshRequest request = new RefreshRequest();
    request.setRefreshToken("refresh-token");

    ValidateResponse validateResponse = new ValidateResponse(true, 10L, Role.ROLE_ADMIN);

    when(jwtService.validateToken("refresh-token")).thenReturn(validateResponse);
    when(jwtService.isRefreshToken("refresh-token")).thenReturn(true);
    when(jwtService.extractLogin("refresh-token")).thenReturn("lefreshz");
    when(dao.findByLogin("lefreshz")).thenReturn(Optional.of(credential));
    when(jwtService.generateAccessToken(credential)).thenReturn("new-access");
    when(jwtService.generateRefreshToken(credential)).thenReturn("new-refresh");

    TokensResponse response = authService.refreshToken(request);

    assertEquals("new-access", response.getAccessToken());
    assertEquals("new-refresh", response.getRefreshToken());
  }

  @Test
  void shouldThrowWhenRefreshTokenInvalid() {
    RefreshRequest request = new RefreshRequest();
    request.setRefreshToken("invalid-token");

    ValidateResponse validateResponse = new ValidateResponse(false, null, null);

    when(jwtService.validateToken("invalid-token")).thenReturn(validateResponse);

    assertThrows(InvalidRefreshTokenException.class, () -> authService.refreshToken(request));
  }

  @Test
  void shouldValidateToken() {
    ValidateRequest request = new ValidateRequest();
    request.setToken("access-token");

    ValidateResponse validateResponse = new ValidateResponse(true, 10L, Role.ROLE_ADMIN);

    when(jwtService.validateToken("access-token")).thenReturn(validateResponse);

    ValidateResponse response = authService.validateToken(request);

    assertTrue(response.isValid());
    assertEquals(10L, response.getUserId());
    assertEquals(Role.ROLE_ADMIN, response.getRole());
  }
}
