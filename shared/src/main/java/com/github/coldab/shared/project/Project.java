package com.github.coldab.shared.project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Project {

  private String name;
  private LocalDateTime creationDate;
  private Collection<File> files = new ArrayList<>();

  public String getName() {
    return name;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public Collection<File> getFiles() {
    return files;
  }
}
