package com.github.coldab.shared.project;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.Chat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private int id;
  @Column(nullable = false)
  private String name;
  @OneToMany
  private final List<Account> admins = new ArrayList<>();
  @OneToMany
  private final List<Account> collaborators = new ArrayList<>();
  @Column(nullable = false)
  private LocalDateTime creationDate = LocalDateTime.now(Clock.systemUTC());

  @Transient
  private final Map<Integer, File> filesById = new HashMap<>();

  @Transient
  private Chat chat;

  public Project() {
  }

  public Project(int id, String name) {
    this.id = id;
    this.name = name;
  }

  @OneToMany
  @Column
  public Collection<File> getFiles() {
    return filesById.values();
  }

  public void setFiles(Collection<File> edits) {
    Map<Integer, File> fileMap = edits.stream()
        .collect(Collectors.toMap(
            File::getId, Function.identity()
        ));
    filesById.clear();
    filesById.putAll(fileMap);
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

  public Map<Integer, File> getFilesById() {
    return filesById;
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
