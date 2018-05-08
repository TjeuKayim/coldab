package com.github.coldab.client.ws;

import com.github.coldab.client.gui.EditorController;
import com.github.coldab.client.project.ChatService;
import com.github.coldab.client.project.ProjectService;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.ServerEndpoint;

public class WebSocketEndpoint implements ClientEndpoint {

  private final ServerEndpoint serverEndpoint;
  private final Project project;
  private final ChatService chatService;
  private ProjectService projectService;

  public WebSocketEndpoint(Project project, Account account,
      EditorController editorController) {
    this.project = project;
    WebSocketConnection webSocketConnection = new WebSocketConnection(project.getId(), this);
    serverEndpoint = webSocketConnection.getServerEndpoint();
    projectService = new ProjectService(project, serverEndpoint.project(), account,
        editorController);
    chatService = new ChatService(project.getChat(), serverEndpoint.chat());
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
