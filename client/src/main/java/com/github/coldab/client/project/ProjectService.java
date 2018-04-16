package com.github.coldab.client.project;

import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ProjectServer;

public class ProjectService implements ProjectClient {

  private final ProjectServer projectServer;
  private final Project project;

  public ProjectService(Project project, ProjectServer projectServer) {
    this.project = project;
    this.projectServer = projectServer;
  }
}
