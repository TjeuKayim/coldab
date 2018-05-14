package com.github.coldab.server.dal;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProjectStore extends CrudRepository<Project, Integer> {

  /**
   * Finds the projects where the Account is listed as a Collaborator.
   * @param account
   * @return A list of projects where the given account is a collaborator.
   *         If no projects are found, this method returns an empty list.
   */
  List<Project> findProjectByCollaboratorsContains(Account account);

  /**
   * Finds the projects where the Account is listed as a admin.
   * @param account
   * @return A list of projects where the given account is a admin.
   *         If no projects are found, this method returns an empty list.
   */
  List<Project> findProjectByAdminsContains(Account account);
}
