package com.github.coldab.shared.rest;

import com.github.coldab.shared.project.Project;
import java.util.Collection;

public interface AccountServer {

  Collection<Project> getProjects(int accountId);

  /**
   * Save a new project in the database and generate id.
   *
   * @return true is successful
   */
  boolean createProject(Project project);
}
