package com.jiramot.auth.resource.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiramot.auth.resource.security.auth.jwt.JwtAuthenticationProvider;
import com.jiramot.auth.resource.security.auth.jwt.JwtHeaderTokenExtractor;
import com.jiramot.auth.resource.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  public static final String AUTHENTICATION_HEADER_NAME = "Authorization";

  @Autowired
  private JwtHeaderTokenExtractor tokenExtractor;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;
  @Autowired
  private AuthenticationFailureHandler failureHandler;

  JwtTokenAuthenticationProcessingFilter createJwtAuthenticationFilter() {
    JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor);
    filter.setAuthenticationManager(this.authenticationManager);
    return filter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {


    http.csrf().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(createJwtAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class);

  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(jwtAuthenticationProvider);
  }
}


