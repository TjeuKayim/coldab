package com.github.coldab.shared.project;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
  private int id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDateTime creationDate;

  @OneToMany
  @Column
  private Collection<File> files = new ArrayList<>();

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
}
