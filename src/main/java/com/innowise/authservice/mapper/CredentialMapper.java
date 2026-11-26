package com.innowise.authservice.mapper;

import com.innowise.authservice.dto.CredentialResponse;
import com.innowise.authservice.dto.SaveCredentialsRequest;
import com.innowise.authservice.entity.Credential;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialMapper {

  Credential toEntity(SaveCredentialsRequest request);

  CredentialResponse toResponse(Credential credential);
}
