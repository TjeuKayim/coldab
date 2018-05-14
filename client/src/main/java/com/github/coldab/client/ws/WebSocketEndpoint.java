package com.github.coldab.client.ws;

import com.github.coldab.client.project.ChatService;
import com.github.coldab.client.project.ProjectComponent;
import com.github.coldab.shared.ws.ClientEndpoint;

public class WebSocketEndpoint implements ClientEndpoint {

  private final ChatService chatService;
  private final ProjectComponent projectComponent;

  public WebSocketEndpoint(ChatService chatService,
      ProjectComponent projectComponent) {
    this.chatService = chatService;
    this.projectComponent = projectComponent;
  }

  @Override
  public ProjectComponent project() {
    return projectComponent;
  }

  @Override
  public ChatService chat() {
    return chatService;
  }
}
