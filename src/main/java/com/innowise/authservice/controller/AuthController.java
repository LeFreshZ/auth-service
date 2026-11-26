package com.innowise.authservice.controller;

import com.innowise.authservice.dto.LoginRequest;
import com.innowise.authservice.dto.RefreshRequest;
import com.innowise.authservice.dto.TokensResponse;
import com.innowise.authservice.dto.ValidateRequest;
import com.innowise.authservice.dto.ValidateResponse;
import com.innowise.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthService service;

  @PostMapping("/login")
  public ResponseEntity<TokensResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.status(200).body(service.login(request));
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokensResponse> refreshToken(@Valid @RequestBody RefreshRequest request) {
    return ResponseEntity.status(200).body(service.refreshToken(request));
  }

  @PostMapping("/validate")
  public ResponseEntity<ValidateResponse> validateToken(
      @Valid @RequestBody ValidateRequest request) {
    return ResponseEntity.status(200).body(service.validateToken(request));
  }
}
