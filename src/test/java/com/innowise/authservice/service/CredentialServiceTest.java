package com.innowise.authservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.authservice.dao.CredentialDao;
import com.innowise.authservice.dto.CredentialResponse;
import com.innowise.authservice.dto.SaveCredentialsRequest;
import com.innowise.authservice.entity.Credential;
import com.innowise.authservice.entity.enums.Role;
import com.innowise.authservice.exception.LoginAlreadyExistsException;
import com.innowise.authservice.mapper.CredentialMapper;
import com.innowise.authservice.service.implementation.CredentialServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class CredentialServiceTest {

  @Mock
  private CredentialDao dao;

  @Mock
  private PasswordEncoder encoder;

  private CredentialService service;

  private Credential credential;

  @BeforeEach
  void setup() {
    CredentialMapper mapper = Mappers.getMapper(CredentialMapper.class);

    service = new CredentialServiceImpl(dao, mapper, encoder);

    credential = new Credential();
    credential.setCredentialId(1L);
    credential.setUserId(10L);
    credential.setLogin("lefreshz");
    credential.setPassword("encodedPassword");
    credential.setRole(Role.ROLE_ADMIN);
  }

  @Test
  void shouldSaveCredentials() {
    SaveCredentialsRequest request = new SaveCredentialsRequest();
    request.setUserId(10L);
    request.setLogin("lefreshz");
    request.setPassword("password");
    request.setRole(Role.ROLE_ADMIN);

    when(dao.existsByLogin("lefreshz")).thenReturn(false);
    when(encoder.encode("password")).thenReturn("encodedPassword");
    when(dao.save(any(Credential.class))).thenReturn(credential);

    CredentialResponse response = service.saveCredentials(request);

    assertEquals("lefreshz", response.getLogin());
    assertEquals(Role.ROLE_ADMIN, response.getRole());

    verify(dao).save(any(Credential.class));
  }

  @Test
  void shouldThrowIfLoginAlreadyExists() {
    SaveCredentialsRequest request = new SaveCredentialsRequest();
    request.setLogin("lefreshz");

    when(dao.existsByLogin("lefreshz")).thenReturn(true);

    assertThrows(LoginAlreadyExistsException.class, () -> service.saveCredentials(request));
  }
}
