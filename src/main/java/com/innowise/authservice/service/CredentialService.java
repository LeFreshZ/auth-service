package com.innowise.authservice.service;

import com.innowise.authservice.dto.CredentialResponse;
import com.innowise.authservice.dto.SaveCredentialsRequest;

/**
 * Service interface for managing user credentials.
 *
 * <p>Provides operations for creating and persisting user credential records
 * within the authentication service.
 */
public interface CredentialService {

  /**
   * Validates, encodes, and persists a new set of user credentials.
   *
   * <p>Ensures the requested login is not already taken, encodes the raw password,
   * saves the resulting {@link com.innowise.authservice.entity.Credential} entity, and returns a
   * response DTO representing the saved record.
   *
   * @param request the request containing the desired login and raw password
   * @return a {@link CredentialResponse} representing the newly created credential
   * @throws com.innowise.authservice.exception.LoginAlreadyExistsException if a credential with the
   *                                                                        given login already
   *                                                                        exists
   */
  CredentialResponse saveCredentials(SaveCredentialsRequest request);
}
