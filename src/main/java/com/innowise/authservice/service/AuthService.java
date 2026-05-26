package com.innowise.authservice.service;

import com.innowise.authservice.dto.LoginRequest;
import com.innowise.authservice.dto.RefreshRequest;
import com.innowise.authservice.dto.TokensResponse;
import com.innowise.authservice.dto.ValidateRequest;
import com.innowise.authservice.dto.ValidateResponse;

public interface AuthService {

  TokensResponse login(LoginRequest request);

  TokensResponse refreshToken(RefreshRequest request);

  ValidateResponse validateToken(ValidateRequest request);
}
