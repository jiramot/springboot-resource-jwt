package com.jiramot.auth.resource.security.exception;

import com.jiramot.auth.resource.security.model.JwtToken;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.AuthenticationException;

public class JwtExpiredTokenException extends AuthenticationException {
  private JwtToken jwtToken;

  public JwtExpiredTokenException(JwtToken jwtToken, String msg, ExpiredJwtException e) {
    super(msg, e);
    this.jwtToken = jwtToken;
  }
}
