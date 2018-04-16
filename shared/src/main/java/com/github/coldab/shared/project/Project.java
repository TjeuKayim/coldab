package com.github.coldab.shared.project;

import com.github.coldab.shared.chat.Chat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Project {
  private int id;
  private String name;
  private LocalDateTime creationDate;
  private Collection<File> files = new ArrayList<>();
  private Chat chat;

  public Project(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public Collection<File> getFiles() {
    return files;
  }

  public int getId() {
    return id;
  }

  public Chat getChat() {
    return chat;
  }
}
