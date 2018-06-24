package com.github.coldab.server.services;

import com.github.coldab.server.dal.AccountStore;
import com.github.coldab.server.dal.FileStore;
import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.server.ws.WebSocketEndpoint;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ChatServer;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.ProjectServer;
import com.github.coldab.shared.ws.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ConnectionManager {

  private final ProjectStore projectStore;
  private final Map<Integer, ProjectSession> projects = new HashMap<>();
  private final Map<ClientEndpoint, ProjectSession> clients = new HashMap<>();
  private final FileStore fileStore;
  private final AccountStore accountStore;

  public ConnectionManager(ProjectStore projectStore,
      FileStore fileStore, AccountStore accountStore) {
    this.projectStore = projectStore;
    this.fileStore = fileStore;
    this.accountStore = accountStore;
  }

  /**
   * Connect client to a project.
   */
  public ServerEndpoint connect(Project project, Account account, ClientEndpoint clientEndpoint) {
    int projectId = project.getId();
    ProjectSession projectSession = projects.computeIfAbsent(projectId, i ->
        new ProjectSession(projectId,
            new ProjectService(project, projectStore, fileStore, accountStore),
            new ChatService()
        ));
    clients.put(clientEndpoint, projectSession);
    ProjectServer projectServer =
        projectSession.projectService.connect(clientEndpoint.project(), account);
    ChatServer chatServer = projectSession.chatService.connect(clientEndpoint.chat(), account);
    return new WebSocketEndpoint(chatServer, projectServer);
  }

  public void disconnect(ClientEndpoint clientEndpoint) {
    ProjectSession projectSession = clients.remove(clientEndpoint);
    projectSession.chatService.disconnect(clientEndpoint.chat());
    projectSession.projectService.disconnect(clientEndpoint.project());
    if (!clients.containsValue(projectSession)) {
      // No one left in this project, so unload it
      projects.remove(projectSession.projectId);
    }
  }

  /**
   * Validates if project exist, and if account is a member.
   *
   * @return Project if valid
   */
  public Project validateConnection(int projectId, Account account) {
    // Load from database
    return projectStore.findById(projectId)
        // Check if account is member
        .filter(account::isMemberOf).orElse(null);
  }

  /**
   * Wraps the services needed for a project.
   */
  private static class ProjectSession {

    final int projectId;
    final ProjectService projectService;
    final ChatService chatService;

    private ProjectSession(int projectId, ProjectService projectService,
        ChatService chatService) {
      this.projectId = projectId;
      this.projectService = projectService;
      this.chatService = chatService;
    }
  }
}
