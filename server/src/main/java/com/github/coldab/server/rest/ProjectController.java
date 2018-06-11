package com.github.coldab.server.rest;

import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.server.services.LoginSessionManager;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account/project")
public class ProjectController {

  private final ProjectStore projectStore;
  private final LoginSessionManager sessionManager;

  public ProjectController(ProjectStore projectStore,
      LoginSessionManager sessionManager) {
    this.projectStore = projectStore;
    this.sessionManager = sessionManager;
  }

  /**
   * Create a new project.
   * @param input contains the project name, and current user as admin
   * @return a empty project with the current user as admin
   */
  @PostMapping
  ResponseEntity<Project> createProject(
      @RequestBody Project input,
      @RequestHeader("Session") String sessionId) {
    Account account = sessionManager.validateSessionId(sessionId);
    String name = input.getName();
    Project project = new Project(name);
    project.getAdmins().add(account);
    projectStore.save(project);
    return ResponseEntity.noContent().build();
  }

  /**
   * Get all projects the current user collaborates on.
   */
  @GetMapping
  Iterable<Project> getProjects(@RequestHeader("Session") String sessionId) {
    Account account = sessionManager.validateSessionId(sessionId);
    return projectStore.findProjectsByUser(account);
  }
}
