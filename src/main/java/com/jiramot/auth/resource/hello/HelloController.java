package com.jiramot.auth.resource.hello;

import com.jiramot.auth.resource.security.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
  @GetMapping("/")
  public String hello(Authentication auth) {
    auth.getPrincipal()

    return "hello " + ((UserContext) auth.getPrincipal()).getUsername();
  }
  }
}
