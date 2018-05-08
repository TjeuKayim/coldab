package com.github.coldab.client.project;

import com.github.coldab.shared.chat.Chat;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.ws.ChatClient;
import com.github.coldab.shared.ws.ChatServer;

public class ChatService implements ChatClient {

  private final Chat chat;
  private final ChatServer server;

  public ChatService(Chat chat, ChatServer server) {
    this.chat = chat;
    this.server = server;
  }

  @Override
  public void message(ChatMessage message) {
    chat.addMessage(message);
  }

  public void sendMessage(ChatMessage message) {
    server.message(message);
  }
}
