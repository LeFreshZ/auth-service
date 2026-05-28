package com.innowise.authservice.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.innowise.authservice.dto.TokensResponse;
import com.innowise.authservice.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class AuthIntegrationTest extends IntegrationTest {

  @Test
  void shouldLoginAndReturnTokens() throws Exception {
    String credentialRequest = createCredentialsRequest(
        "lefreshz",
        "password",
        1L,
        Role.ROLE_USER
    );

    mvc.perform(post("/credentials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(credentialRequest))
        .andExpect(status().isCreated());

    String loginRequest = createLoginRequest("lefreshz", "password");

    mvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.refreshToken").exists());
  }

  @Test
  void shouldBe401IfCredentialsInvalid() throws Exception {
    String loginRequest = createLoginRequest("wrong", "wrong");

    mvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequest))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldValidateToken() throws Exception {
    String credentialRequest = createCredentialsRequest(
        "lefreshz",
        "password",
        1L,
        Role.ROLE_USER
    );

    mvc.perform(post("/credentials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(credentialRequest))
        .andExpect(status().isCreated());

    String loginRequest = createLoginRequest("lefreshz", "password");

    MvcResult result = mvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequest))
        .andExpect(status().isOk())
        .andReturn();

    TokensResponse response = mapper.readValue(result.getResponse().getContentAsString(),
        TokensResponse.class);

    String validateRequest = createValidateRequest(response.getAccessToken());

    mvc.perform(post("/auth/validate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(validateRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.valid").value(true))
        .andExpect(jsonPath("$.userId").value(1L))
        .andExpect(jsonPath("$.role").value("ROLE_USER"));
  }

  @Test
  void shouldRefreshTokens() throws Exception {
    String credentialRequest = createCredentialsRequest(
        "lefreshz",
        "password",
        1L,
        Role.ROLE_USER
    );

    mvc.perform(post("/credentials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(credentialRequest))
        .andExpect(status().isCreated());

    String loginRequest = createLoginRequest("lefreshz", "password");

    MvcResult result = mvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequest))
        .andExpect(status().isOk())
        .andReturn();

    TokensResponse response = mapper.readValue(result.getResponse().getContentAsString(),
        TokensResponse.class);

    String refreshRequest = createRefreshRequest(response.getRefreshToken());

    mvc.perform(post("/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(refreshRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.refreshToken").exists());
  }

  @Test
  void shouldBe401IfRefreshTokenInvalid() throws Exception {
    String refreshRequest = createRefreshRequest("invalid-token");

    mvc.perform(post("/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(refreshRequest))
        .andExpect(status().isUnauthorized());
  }
}
