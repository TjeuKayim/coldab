package com.github.coldab.server.services;

import com.github.coldab.shared.account.Account;

/**
 * A client can connect to a service, and the service returns a server-interface.
 *
 * @param <S> Server interface
 * @param <C> Client interface
 */
public interface Service<S, C> {

  /**
   * Notifies that a new client has connected.
   * @param account The account of the client, used to check permissions.
   */
  S connect(C client, Account account);

  /**
   * Notifies that a client has disconnected.
   */
  void disconnect(C client);
}
