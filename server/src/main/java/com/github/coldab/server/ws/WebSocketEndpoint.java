package com.github.coldab.server.ws;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.ws.ChatClient;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ServerEndpoint;
import com.google.gson.Gson;

public class WebSocketEndpoint implements ClientEndpoint {

  private final ServerEndpoint serverEndpoint;

  public WebSocketEndpoint(ServerEndpoint serverEndpoint) {
    this.serverEndpoint = serverEndpoint;
  }

  @Override
  public ProjectClient project() {
    return null;
  }

  @Override
  public ChatClient chat() {
    return message -> {
      System.out.println("Received chat-message: " + new Gson().toJson(message));
      Account serverTest = new Account("Server test", "server@test.app");
      serverEndpoint.chat().message(
          new ChatMessage("Hi, I received your message!", serverTest));
    };
  }
}
