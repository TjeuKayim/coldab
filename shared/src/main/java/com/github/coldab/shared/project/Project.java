package com.github.coldab.shared.project;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.Chat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Project {

  private int id;
  private String name;
  private final List<Account> admins = new ArrayList<>();
  private final List<Account> collaborators = new ArrayList<>();
  private LocalDateTime creationDate;
  private  final HashMap<Integer, File> files = new HashMap<>();
  private Chat chat;

  public Project(String name) {
    this.name = name;
  }

  public List<Account> getAdmins() {
    return admins;
  }

  public List<Account> getCollaborators() {
    return collaborators;
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
