package com.innowise.authservice.dto;

import com.innowise.authservice.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
  @Size(min = 8)
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d).*$", message = "Password must contain at least one uppercase letter, one lowercase letter and one digit")
  private String password;

  @NotNull
  private Role role;
}
