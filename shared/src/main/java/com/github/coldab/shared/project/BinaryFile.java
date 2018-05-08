package com.github.coldab.shared.project;

import java.time.LocalDateTime;

public class BinaryFile extends File {
  private String hash;

  public BinaryFile(String path, LocalDateTime creationDate) {
    super(path, creationDate);
  }
}
