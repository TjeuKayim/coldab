package com.github.coldab.server.dal;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ProjectStore extends CrudRepository<Project, Integer> {

  /**
   * Finds the projects where the account is listed as a Collaborator.
   *
   * @return A list of projects where the given account is a collaborator. If no projects are found,
   * this method returns an empty list.
   */
  List<Project> findProjectByCollaboratorsContains(Account account);

  /**
   * Finds the projects where the account is listed as a admin.
   *
   * @return A list of projects where the given account is a admin. If no projects are found, this
   * method returns an empty list.
   */
  List<Project> findProjectByAdminsContains(Account account);

  /**
   * Finds the projects where the account is listed as a admin or collaborator
   *
   * @return A list of projects where the given account is a admin or collaborator. If no projects
   * are found, this method returns an empty list.
   */
  default List<Project> findProjectsByUser(Account account) {

    List<Project> result = new ArrayList<>(findProjectByCollaboratorsContains(account));
    result.addAll(findProjectByAdminsContains(account));
    return result;
  }
}
