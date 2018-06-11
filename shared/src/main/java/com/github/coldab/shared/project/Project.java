package com.github.coldab.shared.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.coldab.shared.TimeProvider;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.Chat;
import com.google.gson.annotations.Expose;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
  @Expose
  private int id;
  @Column(nullable = false)
  @Expose
  private String name;
  @OneToMany(fetch = FetchType.EAGER)
  @Expose
  private final Set<Account> admins = new HashSet<>();
  @OneToMany(fetch = FetchType.EAGER)
  @Expose
  private final Set<Account> collaborators = new HashSet<>();
  @Column(nullable = false)
  @Expose
  private LocalDateTime creationDate = TimeProvider.getInstance().now();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonIgnore
  private List<File> files = new ArrayList<>();

  @Transient
  @JsonIgnore
  private final Chat chat = new Chat();

  public Project() {
  }

  public Project(String name) {
    this.name = name;
  }

  public List<File> getFiles() {
    return files;
  }

  public Set<Account> getAdmins() {
    return admins;
  }

  public Set<Account> getCollaborators() {
    return collaborators;
  }

  public String getName() {
    return name;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public File getFileById(int id) {
    return files.stream()
        .filter(f -> f.getId() == id)
        .findAny()
        .orElseThrow(IllegalArgumentException::new);
  }


  public int getId() {
    return id;
  }

  public Chat getChat() {
    return chat;
  }

  public void updateFile(File file) {
    files.stream()
        .filter(f -> f.getId() == file.getId())
        .findAny()
        .ifPresent(files::remove);
    files.add(file);
  }
}
