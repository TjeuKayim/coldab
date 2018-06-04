package com.github.coldab.shared.ws;

public interface ServerEndpoint {

  /**
   * return a object that implements the projectserver interface.
   * @return projectserver interface
   */
  ProjectServer project();

  /**
   * return a object that implements the chatserver interface.
   * @return chatserver interface
   */
  ChatServer chat();
}
