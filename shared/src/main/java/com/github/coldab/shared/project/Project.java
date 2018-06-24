package com.github.coldab.shared.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.coldab.shared.TimeProvider;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.Chat;
import com.google.gson.annotations.Expose;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
  @ManyToMany(fetch = FetchType.EAGER)
  @Expose
  private final Set<Account> admins = new HashSet<>();
  @ManyToMany(fetch = FetchType.EAGER)
  @Expose
  private final Set<Account> collaborators = new HashSet<>();
  @Column(nullable = false)
  @Expose
  private LocalDateTime creationDate = TimeProvider.getInstance().now();

  @OneToMany(fetch = FetchType.EAGER)
  @JsonIgnore
  private Set<File> files = new HashSet<>();

  @Transient
  @JsonIgnore
  private final Chat chat = new Chat();

  public Project() {
  }

  public Project(String name) {
    this.name = name;
  }

  public Set<File> getFiles() {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Project)) {
      return false;
    }
    Project project = (Project) o;
    return id == project.id &&
        Objects.equals(name, project.name) &&
        Objects.equals(admins, project.admins) &&
        Objects.equals(collaborators, project.collaborators) &&
        Objects.equals(creationDate, project.creationDate) &&
        Objects.equals(files, project.files) &&
        Objects.equals(chat, project.chat);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, name, admins, collaborators, creationDate, files, chat);
  }
}
