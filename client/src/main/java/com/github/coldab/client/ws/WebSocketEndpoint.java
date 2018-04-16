package com.github.coldab.client.ws;

import com.github.coldab.client.project.ChatService;
import com.github.coldab.client.project.ProjectService;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ChatClient;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ServerEndpoint;

public class WebSocketEndpoint implements ClientEndpoint {

  private final ServerEndpoint serverEndpoint;
  private final Project project;
  private final ChatService chatService;
  private ProjectService projectService;

  public WebSocketEndpoint(Project project) {
    this.project = project;
    WebSocketConnection webSocketConnection = new WebSocketConnection(project.getId(), this);
    serverEndpoint = webSocketConnection.getServerEndpoint();
    projectService = new ProjectService(project, serverEndpoint.project());
    chatService = new ChatService(project.getChat(), serverEndpoint.chat());
  }

  @Override
  public ProjectClient project() {
    return projectService;
  }

  @Override
  public ChatClient chat() {
    return chatService;
  }
}
