package com.github.coldab.shared.project;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * A binary-file or text-file.
 *
 * <p>
 *   Path is the relative path without leading slash.
 *   Example: "path/to/file.txt"
 * </p>
 */
@MappedSuperclass
public abstract class File {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @Column(unique = true, nullable = false)
  private String path;

  @Column(nullable = false)
  private LocalDateTime creationDate;

  public File(String path, LocalDateTime creationDate) {
    this.path = path.split("/");
    this.creationDate = creationDate;
  }

  public String[] getPath() {
    return path;
  }

  public String getExtension() {
    throw new UnsupportedOperationException();
  }

  public String getName() {
    String[] pathParts = getPath();
    return pathParts[pathParts.length - 1];
  }

  public void setPath(String[] path) {
    this.path = path;
  }

  public int getId() {
    return id;
  }
}
