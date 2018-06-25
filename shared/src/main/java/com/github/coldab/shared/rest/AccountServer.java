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
   */
  boolean createProject(String projectName);

  /**
   * Register a new user.
   */
  Account register(Credentials credentials);

  /**
   * login with an account.
   *
   * @return the account that matches the credentials , if no account matches the credentials return
   * null.
   */
  Account login(Credentials credentials);

  /**
   * logout from the application.
   */
  void logout(String sessionId);

  boolean removeProject(Project project);
}
