package com.github.coldab.client.project;

public interface ProjectObserver {

  /**
   * Files are added or removed.
   */
  void updateFiles();

  void updateCollaborators();
}
