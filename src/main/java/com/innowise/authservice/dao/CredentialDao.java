package com.innowise.authservice.dao;

import com.innowise.authservice.entity.Credential;
import com.innowise.authservice.repository.CredentialRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CredentialDao {

  private final CredentialRepository repository;

  public Optional<Credential> findById(long id) {
    return repository.findById(id);
  }

  public Optional<Credential> findByLogin(String login) {
    return repository.findByLogin(login);
  }

  public boolean existsByLogin(String login) {
    return repository.existsByLogin(login);
  }

  public Credential save(Credential credential) {
    return repository.save(credential);
  }
}
