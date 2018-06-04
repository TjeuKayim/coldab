package com.github.coldab.shared.project;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BinaryFile extends File {

  @Column(nullable = false)
  private String hash;

  public BinaryFile() {
  }

  public BinaryFile(int id, String path, String hash) {
    super(id, path);
    this.hash = hash;
  }

  public String getHash() {
    return hash;
  }
}
