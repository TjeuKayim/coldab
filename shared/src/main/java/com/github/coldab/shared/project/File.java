package com.github.coldab.shared.project;

import java.time.LocalDateTime;

/**
 * A binary-file or text-file.
 *
 * <p>
 *   Path is the relative path without leading slash.
 *   Example: "path/to/file.txt"
 * </p>
 */
public abstract class File {
  private String path;
  private LocalDateTime creationDate;

  public File(String path, LocalDateTime creationDate) {
    this.path = path;
    this.creationDate = creationDate;
  }

  public String getPath() {
    return path;
  }

  public String[] getSplittedPath() {
    return path.split("/");
  }

  public String getExtension() {
    throw new UnsupportedOperationException();
  }
}
