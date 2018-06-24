package com.github.coldab.client.project;

import com.github.coldab.shared.chat.ChatMessage;

public interface ChatController {

  /**
   * send a chatmessage to the server.
   * @param message, the chatmessage you want to send.
   */
  void sendMessage(ChatMessage message);
}
