package com.github.coldab.server.ws;

import com.github.coldab.server.dal.AccountStore;
import com.github.coldab.server.dal.FileStore;
import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ChatServer;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.ProjectServer;
import com.github.coldab.shared.ws.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    ProjectSession projectSession = projects.get(project.getId());
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
      projects.remove(projectSession.project.getId());
    }
  }

  /**
   * Get project from database, and construct a service.
   *
   * @return null if project doesn't exist
   */
  public Project getProject(int projectId) {
    ProjectSession projectSession = projects.get(projectId);
    if (projectSession == null) {
      // Load from database
      Optional<Project> optionalProject = projectStore.findById(projectId);
      if (optionalProject.isPresent()) {
        Project project = optionalProject.get();
        projectSession = new ProjectSession(project,
            new ProjectService(project, projectStore, fileStore, accountStore),
            new ChatService()
        );
        projects.put(projectId, projectSession);
      } else {
        return null;
      }
    }
    return projectSession.project;
  }

  /**
   * Wraps the services needed for a project.
   */
  private static class ProjectSession {

    final ProjectService projectService;
    final ChatService chatService;
    final Project project;

    private ProjectSession(Project project, ProjectService projectService,
        ChatService chatService) {
      this.projectService = projectService;
      this.chatService = chatService;
      this.project = project;
    }
  }
}
