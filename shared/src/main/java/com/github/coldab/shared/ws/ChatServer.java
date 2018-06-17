package com.github.coldab.shared.ws;

import com.github.coldab.shared.chat.ChatMessage;

public interface ChatServer {

  /**
   *
   * @param message The ChatMessage that get`s send to the server
   */

  void message(ChatMessage message);
}
