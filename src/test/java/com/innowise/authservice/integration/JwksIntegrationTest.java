package com.innowise.authservice.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;

class JwksIntegrationTest extends IntegrationTest {

  @Test
  void shouldReturnJwks() throws Exception {
    mvc.perform(get("/.well-known/jwks.json"))
        .andExpect(jsonPath("$.keys").isArray())
        .andExpect(jsonPath("$.keys[0].kty").value("RSA"))
        .andExpect(jsonPath("$.keys[0].kid").value("auth-key"))
        .andExpect(jsonPath("$.keys[0].n").exists())
        .andExpect(jsonPath("$.keys[0].e").exists());
  }
}
