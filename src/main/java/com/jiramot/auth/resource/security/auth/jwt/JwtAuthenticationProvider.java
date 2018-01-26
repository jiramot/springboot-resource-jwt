package com.jiramot.auth.resource.security.auth.jwt;

import com.jiramot.auth.resource.security.JwtAuthenticationToken;
import com.jiramot.auth.resource.security.JwtSettings;
import com.jiramot.auth.resource.security.model.RawAccessJwtToken;
import com.jiramot.auth.resource.security.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
  private final JwtSettings jwtSettings;

  @Autowired
  public JwtAuthenticationProvider(JwtSettings jwtSettings) {
    this.jwtSettings = jwtSettings;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

    Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
    String subject = jwsClaims.getBody().getSubject();
    String uuid = jwsClaims.getBody().get("uuid", String.class);
    List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
    List<GrantedAuthority> authorities = scopes.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    UserContext context = UserContext.create(subject, uuid, authorities);

    return new JwtAuthenticationToken(context, context.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
