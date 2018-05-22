package com.github.coldab.shared.project;

import com.github.coldab.shared.TimeProvider;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.Chat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
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
  private LocalDateTime creationDate = TimeProvider.getInstance().now();

  @OneToMany(cascade = CascadeType.ALL)
  private transient final List<File> files = new ArrayList<>();

  @Transient
  private transient Chat chat;

  public Project() {
  }

  public Project(String name) {
    this.name = name;
  }

  public Collection<File> getFiles() {
    return files;
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

  public void setChat(Chat chat) {
    this.chat = chat;
  }
}
