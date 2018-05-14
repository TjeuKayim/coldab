package com.github.coldab.server.ws;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.ws.ChatClient;
import com.github.coldab.shared.ws.ChatServer;
import com.github.coldab.shared.ws.ClientEndpoint;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatService implements ChatServer {
  private static final Logger LOGGER = Logger.getLogger(WebSocketEndpoint.class.getName());
  private final Collection<ClientEndpoint> projectClients;
  private final ChatClient client;

  public ChatService(
      Collection<ClientEndpoint> projectClients, ChatClient client) {
    this.projectClients = projectClients;
    this.client = client;
  }

  @Override
  public void message(ChatMessage message) {
    // Send message to everyone
    for (ClientEndpoint collaborator : projectClients) {
      collaborator.chat().message(message);
    }
    LOGGER.log(Level.INFO, "Received chat-message: {0}", message);
    Account serverTest = new Account("Server test", "server@test.app");
    client.message(
        new ChatMessage("Hi, I received your message!", serverTest));

  }
}
