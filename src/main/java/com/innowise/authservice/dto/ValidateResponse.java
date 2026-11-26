package com.innowise.authservice.dto;

import com.innowise.authservice.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidateResponse {

  private boolean valid;
  private Long userId;
  private Role role;
}
