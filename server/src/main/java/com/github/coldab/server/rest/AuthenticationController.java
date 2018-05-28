package com.github.coldab.server.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/authentication/")
public class AuthenticationController {

  @RequestMapping(value = "user")
  public Principal user(Principal principal) {
    return principal;
  }

//  @RequestMapping(value = "succes")
//  public boolean succes() {
//    return false;
//  }
}
