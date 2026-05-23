package io.github.lefreshz.auth_service.entity;

import io.github.lefreshz.auth_service.entity.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "credentials")
@Getter
@Setter
public class Credential {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "credential_id")
  private Long credentialId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false)
  private String login;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;
}
