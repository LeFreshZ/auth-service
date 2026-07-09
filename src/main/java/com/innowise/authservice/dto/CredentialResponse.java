package com.innowise.authservice.dto;

import com.innowise.authservice.entity.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialResponse {

  private Long credentialId;
  private Long userId;
  private String login;
  private Role role;
}
