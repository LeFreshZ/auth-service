package com.innowise.authservice.service;

import com.innowise.authservice.dto.CredentialResponse;
import com.innowise.authservice.dto.SaveCredentialsRequest;

public interface CredentialService {

  CredentialResponse saveCredentials(SaveCredentialsRequest request);
}
