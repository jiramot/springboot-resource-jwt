package com.jiramot.auth.resource.security.auth.jwt;

public interface TokenVerifier {
  public boolean verify(String jti);
}
