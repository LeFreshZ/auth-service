package com.innowise.authservice.controller;

import com.innowise.authservice.service.implementation.JwtServiceImpl;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class JwksController {

  private JwtServiceImpl jwtService;

  @GetMapping("/.well-known/jwks.json")
  public Map<String, Object> jwks() {
    RSAKey rsaKey = new RSAKey.Builder(jwtService.getPublicKey())
        .keyID("auth-key")
        .build();

    return new JWKSet(rsaKey).toJSONObject();
  }
}
