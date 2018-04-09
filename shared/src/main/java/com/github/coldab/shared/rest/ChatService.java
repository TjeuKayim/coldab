package com.github.coldab.shared.rest;

import com.github.coldab.shared.chat.Message;

public interface ChatService {
  void sendMessage(Message message);
}
