package com.innowise.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateRequest {

  @NotBlank
  private String token;
}
