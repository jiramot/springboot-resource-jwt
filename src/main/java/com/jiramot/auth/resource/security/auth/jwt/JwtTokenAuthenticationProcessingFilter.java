package com.jiramot.auth.resource.security.auth.jwt;

import com.jiramot.auth.resource.security.JwtAuthenticationToken;
import com.jiramot.auth.resource.security.WebSecurityConfig;
import com.jiramot.auth.resource.security.model.RawAccessJwtToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
  private final AuthenticationFailureHandler failureHandler;
  private final JwtHeaderTokenExtractor tokenExtractor;

  public JwtTokenAuthenticationProcessingFilter(AuthenticationFailureHandler failureHandler, JwtHeaderTokenExtractor tokenExtractor) {
    super("/**");
    this.failureHandler = failureHandler;
    this.tokenExtractor = tokenExtractor;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    String tokenPayload = request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME);
    RawAccessJwtToken token = new RawAccessJwtToken(tokenExtractor.extract(tokenPayload));
    return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authResult);
    SecurityContextHolder.setContext(context);
    chain.doFilter(request, response);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    failureHandler.onAuthenticationFailure(request, response, failed);
  }

}
