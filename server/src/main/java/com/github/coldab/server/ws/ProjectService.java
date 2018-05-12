package com.github.coldab.server.ws;

import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.ProjectServer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectService {
  private final Project project;
  private final List<ClientEndpoint> clients = new ArrayList<>();

  ProjectService(Project project) {
    this.project = project;
  }

  public ProjectServer addClient(ClientEndpoint clientEndpoint) {
    clients.add(clientEndpoint);
    return new MessageReceiver();
  }

  public void removeClient(ClientEndpoint clientEndpoint) {
    clients.remove(clientEndpoint);
  }

  public List<ClientEndpoint> getClients() {
    return Collections.unmodifiableList(clients);
  }

  private class MessageReceiver implements ProjectServer {
    
  }
}
