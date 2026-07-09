package com.innowise.authservice.service;

import com.innowise.authservice.dto.LoginRequest;
import com.innowise.authservice.dto.RefreshRequest;
import com.innowise.authservice.dto.TokensResponse;
import com.innowise.authservice.dto.ValidateRequest;
import com.innowise.authservice.dto.ValidateResponse;

/**
 * Service interface for authentication operations.
 *
 * <p>Provides methods for user login, token refresh, and token validation
 * within the authentication service.
 */
public interface AuthService {

  /**
   * Authenticates a user using the provided credentials and returns a pair of JWT tokens.
   *
   * <p>Verifies that the user exists and that the supplied password matches the stored
   * encoded password. On success, issues a new access token and refresh token.
   *
   * @param request the login request containing the user's login and password
   * @return a {@link TokensResponse} containing a fresh access token and refresh token
   * @throws com.innowise.authservice.exception.InvalidCredentialsException if no user is found for
   *                                                                        the given login or the
   *                                                                        password does not match
   */
  TokensResponse login(LoginRequest request);

  /**
   * Issues a new pair of JWT tokens in exchange for a valid refresh token.
   *
   * <p>Validates that the supplied token is a legitimate, non-expired refresh token,
   * then looks up the associated user and generates fresh access and refresh tokens.
   *
   * @param request the refresh request containing the refresh token
   * @return a {@link TokensResponse} containing a new access token and refresh token
   * @throws com.innowise.authservice.exception.InvalidRefreshTokenException if the token is
   *                                                                         expired, malformed, or
   *                                                                         is not a refresh token
   * @throws com.innowise.authservice.exception.InvalidCredentialsException  if the user encoded in
   *                                                                         the token no longer
   *                                                                         exists
   */
  TokensResponse refreshToken(RefreshRequest request);

  /**
   * Validates the given JWT token and returns its validation result.
   *
   * <p>Delegates validation to {@link com.innowise.authservice.service.JwtService}
   * and returns its response, indicating whether the token is valid along with any additional
   * metadata supplied by the JWT service.
   *
   * @param request the validation request containing the token to validate
   * @return a {@link ValidateResponse} describing the validity of the token
   */
  ValidateResponse validateToken(ValidateRequest request);
}
