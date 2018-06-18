package com.github.coldab.shared.ws;

public interface ClientEndpoint {

  /**
   * Return object that implements the projectclient interface.
   */
  ProjectClient project();

  /**
   * return object that implements the chatclient interface.
   */

  ChatClient chat();
}
