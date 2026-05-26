package com.innowise.authservice.service;

import com.innowise.authservice.dto.ValidateResponse;
import com.innowise.authservice.entity.Credential;

public interface JwtService {

  String generateAccessToken(Credential credential);

  String generateRefreshToken(Credential credential);

  ValidateResponse validateToken(String token);

  String extractLogin(String token);

  boolean isRefreshToken(String token);
}
