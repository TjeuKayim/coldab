package com.github.coldab.server.rest;

import com.github.coldab.shared.account.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/account/login", method = RequestMethod.POST)
public class AccountController {

  /**
   * Login with account
   *
   * @param login
   * @return jwt token
   */
  @PostMapping
  public String login(@RequestBody Account login) {

    //TODO: return jwt token??
    return "test";
  }
}
