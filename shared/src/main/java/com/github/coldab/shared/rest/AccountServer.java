package com.github.coldab.shared.rest;

import com.github.coldab.shared.project.Project;

public interface AccountServer {

  Iterable<Project> getProjects(int accountId);

  /**
   * Save a new project in the database and generate id.
   *
   * @return true is successful
   */
  boolean createProject(Project project);
}
