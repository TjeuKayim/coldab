package com.github.coldab.shared.project;

import com.github.coldab.shared.TimeProvider;
import com.google.gson.annotations.Expose;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * A binary-file or text-file.
 *
 * <p>
 * Path is the relative path without leading slash. Example: "path/to/file.txt"
 * </p>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class File {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  @Expose
  private int id;

  @Column(nullable = false)
  @Expose
  private String path;

  @Column(nullable = false)
  @Expose
  private final LocalDateTime creationDate = TimeProvider.getInstance().now();

  public File() {
  }

  public File(int id, String path) {
    this.id = id;
    this.path = path;
  }

  public String[] getPath() {
    return path.split("/");
  }

  public String getExtension() {
    throw new UnsupportedOperationException();
  }

  public String getName() {
    String[] pathParts = getPath();
    return pathParts[pathParts.length - 1];
  }

  public void setPath(String[] path) {
    this.path = String.join("/", path);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof File)) {
      return false;
    }
    File file = (File) o;
    return id == file.id &&
        Objects.equals(path, file.path) &&
        Objects.equals(creationDate, file.creationDate);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, path, creationDate);
  }

  public int getId() {
    return id;
  }
}
