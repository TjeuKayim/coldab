package com.github.coldab.client.ws;

import com.github.coldab.client.project.ChatService;
import com.github.coldab.client.project.ProjectService;
import com.github.coldab.shared.ws.ClientEndpoint;

public class WebSocketEndpoint implements ClientEndpoint {

  private final ChatService chatService;
  private final ProjectService projectService;

  public WebSocketEndpoint(ChatService chatService,
      ProjectService projectService) {
    this.chatService = chatService;
    this.projectService = projectService;
  }

  @Override
  public ProjectService project() {
    return projectService;
  }

  @Override
  public ChatService chat() {
    return chatService;
  }
}
