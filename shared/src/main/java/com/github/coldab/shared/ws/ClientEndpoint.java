package com.github.coldab.shared.ws;

public interface ClientEndpoint {

  /**
   * Return object that implements the projectclient interface.
   * @return projectclient implementation.
   */
  ProjectClient project();

  /**
   * return object that implements the chatclient interface.
   * @return chatclient implementation.
   */

  ChatClient chat();
}
