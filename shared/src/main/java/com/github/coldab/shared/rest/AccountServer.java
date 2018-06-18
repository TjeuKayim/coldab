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

  /**
   * Register a new user.
   *
   * @param credentials containing the username and password.
   * @return the new account.
   */
  Account register(Credentials credentials);

  /**
   * login with an account.
   *
   * @param credentials the username and password of the account.
   * @return the account that matches the credentials , if no account matches the credentials return
   * null.
   */
  Account login(Credentials credentials);

  /**
   * logout from the application.
   * @param sessionId the id of the current session.
   */
  void logout(String sessionId);
}
