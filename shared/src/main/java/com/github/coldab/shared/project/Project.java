package com.github.coldab.shared.project;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.Chat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private final int id;
  @Column(nullable = false)
  private final String name;
  private final List<Account> admins = new ArrayList<>();
  private final List<Account> collaborators = new ArrayList<>();
  @Column(nullable = false)
  private LocalDateTime creationDate;

  @OneToMany
  @Column
  private  final Map<Integer, File> files = new HashMap<>();
  private Chat chat;

  public Project(int id, String name) {
    this.id = id;
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

  public Map<Integer, File> getFiles() {
    return files;
  }


  public int getId() {
    return id;
  }

  public Chat getChat() {
    return chat;
  }

  public void setChat(Chat chat) {
    this.chat = chat;
  }
}
