package com.github.coldab.shared.project;

import com.github.coldab.shared.chat.Chat;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Project {
  private int id;
  private String name;
  private LocalDateTime creationDate;
  private HashMap<Integer, File> files = new HashMap<>();
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

  public HashMap<Integer, File> getFiles() {
    return files;
  }

  public int getId() {
    return id;
  }

  public Chat getChat() {
    return chat;
  }
}
