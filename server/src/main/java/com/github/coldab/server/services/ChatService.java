package com.github.coldab.server.services;

import com.github.coldab.server.ws.WebSocketEndpoint;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.ws.ChatClient;
import com.github.coldab.shared.ws.ChatServer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Serves the chat for a project.
 */
public class ChatService implements Service<ChatServer, ChatClient> {
  private static final Logger LOGGER = Logger.getLogger(WebSocketEndpoint.class.getName());
  private final List<ChatClient> projectClients = new ArrayList<>();

  @Override
  public ChatServer connect(ChatClient client, Account account) {
    projectClients.add(client);
    return new MessageReceiver(account);
  }

  @Override
  public void disconnect(ChatClient client) {
    projectClients.remove(client);
  }

  private class MessageReceiver implements ChatServer {
    private final Account account;

    public MessageReceiver(Account account) {
      this.account = account;
    }


    @Override
    public void message(ChatMessage message) {
      // Check if author is correct
      if (!account.equals(message.getAuthor())) {
        LOGGER.severe("Message received with invalid author");
        return;
      }
      // Send message to everyone
      for (ChatClient collaborator : projectClients) {
        collaborator.message(message);
      }
    }
  }
}
