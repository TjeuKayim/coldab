package com.github.coldab.client.project;

public interface ProjectObserver {

  /**
   * Files are added or removed.
   */
  void updateFiles();

  /**
   * collaborators are added or removed.
   */
  void updateCollaborators();
}
