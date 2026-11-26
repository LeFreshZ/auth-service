package com.innowise.authservice.service.implementation;

import com.innowise.authservice.config.JwtProperties;
import com.innowise.authservice.dto.ValidateResponse;
import com.innowise.authservice.entity.Credential;
import com.innowise.authservice.entity.enums.Role;
import com.innowise.authservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

  public static final String TOKEN_TYPE_CLAIM = "tokenType";
  public static final String ACCESS = "ACCESS";
  public static final String REFRESH = "REFRESH";

  private final JwtProperties properties;
  private final RSAPrivateKey privateKey;
  @Getter
  private final RSAPublicKey publicKey;

  public JwtServiceImpl(JwtProperties properties) throws Exception {
    this.properties = properties;
    this.privateKey = loadPrivateKey(properties.getPrivateKeyPath());
    this.publicKey = loadPublicKey(properties.getPublicKeyPath());
  }

  @Override
  public String generateAccessToken(Credential credential) {
    return buildToken(credential, properties.getAccessTokenExpiration(), ACCESS);
  }

  @Override
  public String generateRefreshToken(Credential credential) {
    return buildToken(credential, properties.getRefreshTokenExpiration(), REFRESH);
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

    return claims.get(TOKEN_TYPE_CLAIM, String.class).equals(REFRESH);
  }

  private String buildToken(Credential credential, long expiration, String tokenType) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .subject(credential.getLogin())
        .claim(TOKEN_TYPE_CLAIM, tokenType)
        .claim("userId", credential.getUserId())
        .claim("role", credential.getRole().name())
        .issuedAt(now)
        .expiration(expirationDate)
        .signWith(privateKey)
        .compact();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(publicKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private RSAPrivateKey loadPrivateKey(String path) throws Exception {
    Resource resource = new ClassPathResource(path.replace("classpath:", ""));
    String pem = new String(resource.getInputStream().readAllBytes())
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replaceAll("\\s", "");

    byte[] decoded = Base64.getDecoder().decode(pem);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

    return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
  }

  private RSAPublicKey loadPublicKey(String path) throws Exception {
    Resource resource = new ClassPathResource(path.replace("classpath:", ""));
    String pem = new String(resource.getInputStream().readAllBytes())
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replaceAll("\\s", "");

    byte[] decoded = Base64.getDecoder().decode(pem);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

    return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
  }
}
