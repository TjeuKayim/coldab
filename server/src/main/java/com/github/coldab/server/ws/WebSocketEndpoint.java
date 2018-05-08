package com.github.coldab.server.ws;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.ws.ChatServer;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.ProjectServer;
import com.github.coldab.shared.ws.ServerEndpoint;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebSocketEndpoint implements ServerEndpoint {

  private final ClientEndpoint client;
  private final List<ClientEndpoint> projectClients;
  private static final Logger LOGGER = Logger.getLogger(WebSocketEndpoint.class.getName());

  public WebSocketEndpoint(ClientEndpoint client,
      List<ClientEndpoint> projectClients) {
    this.client = client;
    this.projectClients = projectClients;
  }

  @Override
  public ProjectServer project() {
    return null;
  }

  @Override
  public ChatServer chat() {
    return message -> {
      // Send message to everyone
      for (ClientEndpoint collaborator : projectClients) {
        collaborator.chat().message(message);
      }
      LOGGER.log(Level.INFO, "Received chat-message: {0}", message);
      Account serverTest = new Account("Server test", "server@test.app");
      client.chat().message(
          new ChatMessage("Hi, I received your message!", serverTest));
    };
  }
}
