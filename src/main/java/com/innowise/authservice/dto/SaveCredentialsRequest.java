package com.innowise.authservice.dto;

import com.innowise.authservice.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveCredentialsRequest {

  @NotNull
  private Long userId;

  @NotBlank
  private String login;

  @NotBlank
  private String password;

  @NotNull
  private Role role;
}
