package com.innowise.authservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

  private String privateKeyPath;
  private String publicKeyPath;
  private long accessTokenExpiration;
  private long refreshTokenExpiration;
}
