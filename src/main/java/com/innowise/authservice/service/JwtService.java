package com.innowise.authservice.service;

import com.innowise.authservice.dto.ValidateResponse;
import com.innowise.authservice.entity.Credential;

/**
 * Service interface for JWT token operations.
 *
 * <p>Provides methods for generating, validating, and inspecting JSON Web Tokens
 * used for authentication and authorization within the auth service. Tokens are signed with an
 * HMAC-SHA key and carry claims such as user ID, role, and token type.
 */
public interface JwtService {

  /**
   * Generates a signed JWT access token for the given credential.
   *
   * <p>The token is issued with a {@code tokenType} claim of {@code "ACCESS"},
   * a subject set to the user's login, and an expiration determined by the configured access token
   * TTL.
   *
   * @param credential the credential whose login, user ID, and role are embedded in the token
   * @return a compact, signed JWT access token string
   */
  String generateAccessToken(Credential credential);

  /**
   * Generates a signed JWT refresh token for the given credential.
   *
   * <p>The token is issued with a {@code tokenType} claim of {@code "REFRESH"},
   * a subject set to the user's login, and an expiration determined by the configured refresh token
   * TTL.
   *
   * @param credential the credential whose login, user ID, and role are embedded in the token
   * @return a compact, signed JWT refresh token string
   */
  String generateRefreshToken(Credential credential);

  /**
   * Validates the given JWT token and extracts its principal claims.
   *
   * <p>Parses and verifies the token signature and expiration. On success,
   * extracts the {@code userId} and {@code role} claims and returns them alongside a
   * {@code valid = true} flag. If parsing or verification fails for any reason, returns a response
   * with {@code valid = false} and {@code null} claim values.
   *
   * @param token the compact JWT string to validate
   * @return a {@link ValidateResponse} indicating validity and, when valid, the user ID and role
   * embedded in the token
   */
  ValidateResponse validateToken(String token);

  /**
   * Extracts the login (subject) from the given JWT token.
   *
   * <p>Parses the token claims and returns the value of the {@code sub} field,
   * which corresponds to the credential's login.
   *
   * @param token the compact JWT string to extract the login from
   * @return the login string stored as the token subject
   * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed
   */
  String extractLogin(String token);

  /**
   * Determines whether the given JWT token is a refresh token.
   *
   * <p>Inspects the {@code tokenType} claim and returns {@code true}
   * only if its value equals {@code "REFRESH"}.
   *
   * @param token the compact JWT string to inspect
   * @return {@code true} if the token carries a {@code tokenType} of {@code "REFRESH"},
   * {@code false} otherwise
   * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed
   */
  boolean isRefreshToken(String token);
}
