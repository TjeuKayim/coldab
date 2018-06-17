package com.github.coldab.server.ws;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.ws.ChatClient;
import com.github.coldab.shared.ws.ChatServer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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
    return new MessageReceiver(client, account);
  }

  @Override
  public void disconnect(ChatClient client) {
    projectClients.remove(client);
  }

  private class MessageReceiver implements ChatServer {
    private final ChatClient client;
    private final Account account;

    public MessageReceiver(ChatClient client, Account account) {
      this.client = client;
      this.account = account;
    }

    @Override
    public void message(ChatMessage message) {
      // Check if author is correct
      if (message.getAuthor() != account) {
        LOGGER.severe("Message received with invalid author");
        return;
      }
      // Send message to everyone
      for (ChatClient collaborator : projectClients) {
        collaborator.message(message);
      }
      LOGGER.log(Level.INFO, "Received chat-message: {0}", message);
      Account serverTest = new Account("Server test", "server@test.app");
      client.message(new ChatMessage("Hi, I received your message!", serverTest));
    }
  }
}
