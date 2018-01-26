package com.jiramot.auth.resource.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth.jwt")
@Data
public class JwtSettings {
  private String tokenSigningKey;
}