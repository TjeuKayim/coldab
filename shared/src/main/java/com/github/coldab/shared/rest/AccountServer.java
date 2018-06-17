package com.github.coldab.shared.rest;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import java.util.List;

public interface AccountServer {

  /**
   * Get a list of projects
   *
   * @return A list with projects, if there are no projects it returns an empty list.
   */

  List<Project> getProjects();

  /**
   * Save a new project in the database and generate id.
   *
   * @return true if successful
   */
  boolean createProject(String projectName);

  Account register(Credentials credentials);

  Account login(Credentials credentials);

  void logout(String sessionId);
}
