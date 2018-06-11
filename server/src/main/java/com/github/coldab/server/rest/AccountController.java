package com.github.coldab.server.rest;

import com.github.coldab.server.dal.AccountStore;
import com.github.coldab.shared.account.Account;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/account/", method = RequestMethod.POST)
public class AccountController {
  private final AccountStore accountStore;

  public AccountController(AccountStore accountStore) {
    this.accountStore = accountStore;
  }

  /**
   * Login with account
   *
   * @return account met SID
   */
  @PostMapping("login")
  public Account login(String email, String password) {
    Account account = accountStore.findAccountByemail(email);
    if (account != null) {
      //TODO: check if password is correct

    }

    return null;
  }

  @PostMapping("register")
  public Account register(String email, String password) {
    return null;
  }

  @PostMapping("logout")
  public void logout(String sessionId) {

  }
}
