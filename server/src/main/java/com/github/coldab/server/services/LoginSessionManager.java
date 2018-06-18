package com.github.coldab.server.services;

import com.github.coldab.shared.account.Account;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class LoginSessionManager {

  private Map<String, Account> sessions = new HashMap<>();

  /** Create a new session at login. */
  public void login(Account account) {
    String guid = UUID.randomUUID().toString();
    account.setSessionId(guid);
    sessions.put(guid, account);
  }

  /**
   * Retrieves account for a session-id.
   * @return the logged-in user, or null if session is invalid
   */
  public Account validateSessionId(String sessionId) {
    return sessions.get(sessionId);
  }

  /**
   * logout a account , and delete the session.
   * @param sessionId the session id you want to delete.
   * @return the account you logged out, null if the sessionid dosn`t exist.
   */
  public Account logout(String sessionId) {
    return sessions.remove(sessionId);
  }
}
