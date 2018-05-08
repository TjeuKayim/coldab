package com.github.coldab.client.project;

import com.github.coldab.client.gui.EditorController;
import com.github.coldab.shared.chat.Chat;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.ws.ChatClient;
import com.github.coldab.shared.ws.ChatServer;

public class ChatService implements ChatClient {

  private final Chat chat;
  private final ChatServer chatServer;

  public ChatService(Chat chat, ChatServer chatServer,
      EditorController editorController) {
    this.chat = chat;
    this.chatServer = chatServer;
  }

  @Override
  public void message(ChatMessage message) {

  }
}
