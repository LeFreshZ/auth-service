package com.innowise.authservice.service.implementation;

import com.innowise.authservice.dao.CredentialDao;
import com.innowise.authservice.dto.CredentialResponse;
import com.innowise.authservice.dto.SaveCredentialsRequest;
import com.innowise.authservice.entity.Credential;
import com.innowise.authservice.exception.LoginAlreadyExistsException;
import com.innowise.authservice.mapper.CredentialMapper;
import com.innowise.authservice.service.CredentialService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CredentialServiceImpl implements CredentialService {

  private final CredentialDao dao;
  private final CredentialMapper mapper;
  private final PasswordEncoder encoder;

  @Override
  public CredentialResponse saveCredentials(SaveCredentialsRequest request) {
    if (dao.existsByLogin(request.getLogin())) {
      throw new LoginAlreadyExistsException("User with login \"" + request.getLogin() + "\" "
          + "already exists");
    }

    Credential credential = mapper.toEntity(request);
    credential.setPassword(encoder.encode(request.getPassword()));

    Credential savedCredential = dao.save(credential);

    return mapper.toResponse(savedCredential);
  }

  @Override
  @Transactional
  public void deleteCredentialsByUserId(Long userId) {
    dao.deleteByUserId(userId);
  }
}
