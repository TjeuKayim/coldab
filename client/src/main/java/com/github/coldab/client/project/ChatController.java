package com.github.coldab.client.project;

import com.github.coldab.shared.chat.ChatMessage;

public interface ChatController {

  void sendMessage(ChatMessage message);
}
