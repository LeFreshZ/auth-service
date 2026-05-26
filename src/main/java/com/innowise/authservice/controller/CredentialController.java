package com.innowise.authservice.controller;

import com.innowise.authservice.dto.CredentialResponse;
import com.innowise.authservice.dto.SaveCredentialsRequest;
import com.innowise.authservice.service.CredentialService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credentials")
@AllArgsConstructor
public class CredentialController {

  private final CredentialService service;

  @PostMapping
  public ResponseEntity<CredentialResponse> saveCredentials(
      @Valid @RequestBody SaveCredentialsRequest request) {
    return ResponseEntity.status(201).body(service.saveCredentials(request));
  }
}
