package com.github.coldab.client.dal;

import com.github.coldab.shared.project.Project;
import java.util.Collection;

/**
 * Stores project-files in a local directory, and remembers the path.
 */
public interface ProjectStore {

  /**
   * find the recently opend projects.
   * @return a collection of recent projects.
   */
  Collection<Project> recentProjects();
}
