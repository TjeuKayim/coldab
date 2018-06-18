package com.github.coldab.client.ws;

import com.github.coldab.client.project.ChatComponent;
import com.github.coldab.client.project.ProjectComponent;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.ProjectClient;

public class WebSocketEndpoint implements ClientEndpoint {

  private final ChatComponent chatComponent;
  private final ProjectComponent projectComponent;

  public WebSocketEndpoint(ChatComponent chatComponent,
      ProjectComponent projectComponent) {
    this.chatComponent = chatComponent;
    this.projectComponent = projectComponent;
  }

  @Override
  public ProjectClient project() {
    return projectComponent;
  }

  @Override
  public ChatComponent chat() {
    return chatComponent;
  }
}
