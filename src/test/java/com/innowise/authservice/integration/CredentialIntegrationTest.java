package com.innowise.authservice.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.innowise.authservice.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class CredentialIntegrationTest extends IntegrationTest {

  @Test
  void shouldSaveCredentials() throws Exception {
    String request = createCredentialsRequest(
        "lefreshz",
        "password",
        1L,
        Role.ROLE_USER
    );

    mvc.perform(post("/credentials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.login").value("lefreshz"))
        .andExpect(jsonPath("$.userId").value(1L))
        .andExpect(jsonPath("$.role").value("ROLE_USER"));
  }

  @Test
  void shouldBe409IfLoginAlreadyExists() throws Exception {
    String request = createCredentialsRequest(
        "lefreshz",
        "password",
        1L,
        Role.ROLE_USER
    );

    mvc.perform(post("/credentials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isCreated());

    mvc.perform(post("/credentials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
        .andExpect(status().isConflict());
  }
}
