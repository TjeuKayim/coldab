package com.github.coldab.server.rest;

import com.github.coldab.server.dal.AccountStore;
import com.github.coldab.server.services.LoginSessionManager;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.rest.Credentials;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account/")
public class AccountController {
  private final AccountStore accountStore;
  private final LoginSessionManager sessionManager;

  public AccountController(AccountStore accountStore,
      LoginSessionManager sessionManager) {
    this.accountStore = accountStore;
    this.sessionManager = sessionManager;
  }

  /**
   * Login with account
   *
   * @return account with SID
   */
  @PostMapping("login")
  public Account login(@RequestBody Credentials credentials) {
    Account account = accountStore.findAccountByEmail(credentials.getEmail());
    if (account != null) {
      if (account.validatePassword(credentials.getPassword())) {
        // Valid login
        sessionManager.login(account);
        return account;
      } else {
        // Invalid login
        return null;
      }
    }
    return null;
  }

  @PostMapping("register")
  public Account register(@RequestBody Credentials credentials) {
    Account account = new Account(null, credentials.getEmail(), credentials.getPassword());
    account = accountStore.save(account);
    sessionManager.login(account);
    return account;
  }

  @PostMapping("logout")
  public boolean logout(@RequestBody String sessionId) {
    sessionManager.logout(sessionId);
    return true;
  }
}
