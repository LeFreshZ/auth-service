package com.innowise.authservice.service.implementation;

import com.innowise.authservice.dao.CredentialDao;
import com.innowise.authservice.dto.LoginRequest;
import com.innowise.authservice.dto.RefreshRequest;
import com.innowise.authservice.dto.TokensResponse;
import com.innowise.authservice.dto.ValidateRequest;
import com.innowise.authservice.dto.ValidateResponse;
import com.innowise.authservice.entity.Credential;
import com.innowise.authservice.exception.InvalidCredentialsException;
import com.innowise.authservice.exception.InvalidRefreshTokenException;
import com.innowise.authservice.service.AuthService;
import com.innowise.authservice.service.JwtService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final CredentialDao dao;
  private final PasswordEncoder encoder;
  private final JwtService jwtService;

  @Override
  public TokensResponse login(LoginRequest request) {
    Credential credential = findCredentialByLogin(request.getLogin());

    if (!encoder.matches(request.getPassword(), credential.getPassword())) {
      throw new InvalidCredentialsException("Invalid login or password");
    }

    return new TokensResponse(
        jwtService.generateAccessToken(credential),
        jwtService.generateRefreshToken(credential)
    );
  }

  @Override
  public TokensResponse refreshToken(RefreshRequest request) {
    String token = request.getRefreshToken();

    if (!jwtService.validateToken(token).isValid() || !jwtService.isRefreshToken(token)) {
      throw new InvalidRefreshTokenException("The refresh token is not valid");
    }

    Credential credential = findCredentialByLogin(jwtService.extractLogin(token));

    return new TokensResponse(
        jwtService.generateAccessToken(credential),
        jwtService.generateRefreshToken(credential)
    );
  }

  @Override
  public ValidateResponse validateToken(ValidateRequest request) {
    return jwtService.validateToken(request.getToken());
  }

  private Credential findCredentialByLogin(String login) {
    Optional<Credential> optionalCredential = dao.findByLogin(login);

    if (optionalCredential.isEmpty()) {
      throw new InvalidCredentialsException("Invalid login or password");
    }

    return optionalCredential.get();
  }
}
