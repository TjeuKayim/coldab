package com.github.coldab.shared.ws;

import com.github.coldab.shared.chat.ChatMessage;

public interface ChatServer {

  void message(ChatMessage message);
}
