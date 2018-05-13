package com.github.coldab.shared.project;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BinaryFile extends File {
  @Column(nullable = false)
  private String hash;

  public BinaryFile(String path, LocalDateTime creationDate) {
    super(path, creationDate);
  }
}
