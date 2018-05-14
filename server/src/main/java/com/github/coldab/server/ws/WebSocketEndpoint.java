package com.github.coldab.server.ws;

import com.github.coldab.shared.ws.ChatServer;
import com.github.coldab.shared.ws.ProjectServer;
import com.github.coldab.shared.ws.ServerEndpoint;

public class WebSocketEndpoint implements ServerEndpoint {

  private final ChatServer chatServer;
  private final ProjectServer projectServer;

  public WebSocketEndpoint(ChatServer chatServer,
      ProjectServer projectServer) {
    this.chatServer = chatServer;
    this.projectServer = projectServer;
  }

  @Override
  public ProjectServer project() {
    return projectServer;
  }

  @Override
  public ChatServer chat() {
    return chatServer;
  }
}
