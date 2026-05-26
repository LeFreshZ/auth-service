package com.innowise.authservice.service.implementation;

import com.innowise.authservice.config.JwtProperties;
import com.innowise.authservice.dto.ValidateResponse;
import com.innowise.authservice.entity.Credential;
import com.innowise.authservice.entity.enums.Role;
import com.innowise.authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

  private final JwtProperties properties;

  @Override
  public String generateAccessToken(Credential credential) {
    return buildToken(credential, properties.getAccessTokenExpiration(), "ACCESS");
  }

  @Override
  public String generateRefreshToken(Credential credential) {
    return buildToken(credential, properties.getRefreshTokenExpiration(), "REFRESH");
  }

  @Override
  public ValidateResponse validateToken(String token) {
    try {
      Claims claims = extractAllClaims(token);

      Long userId = claims.get("userId", Long.class);
      Role role = Role.valueOf(claims.get("role", String.class));

      return new ValidateResponse(true, userId, role);
    } catch (Exception ex) {
      return new ValidateResponse(false, null, null);
    }
  }

  @Override
  public String extractLogin(String token) {
    return extractAllClaims(token).getSubject();
  }

  @Override
  public boolean isRefreshToken(String token) {
    Claims claims = extractAllClaims(token);

    return claims.get("tokenType", String.class).equals("REFRESH");
  }

  private String buildToken(Credential credential, long expiration, String tokenType) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .subject(credential.getLogin())
        .claim("tokenType", tokenType)
        .claim("userId", credential.getUserId())
        .claim("role", credential.getRole().name())
        .issuedAt(now)
        .expiration(expirationDate)
        .signWith(getKey())
        .compact();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
  }
}
