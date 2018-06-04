package com.github.coldab.client.project;

import com.github.coldab.shared.chat.Chat;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.ws.ChatClient;
import com.github.coldab.shared.ws.ChatServer;

public class ChatComponent implements ChatClient, ChatController {

  private final Chat chat;
  private final ChatServer server;

  public ChatComponent(Chat chat, ChatServer server) {
    this.chat = chat;
    this.server = server;
  }

  @Override
  public void message(ChatMessage message) {
    chat.addMessage(message);
  }

  @Override
  public void sendMessage(ChatMessage message) {
    server.message(message);
  }
}
