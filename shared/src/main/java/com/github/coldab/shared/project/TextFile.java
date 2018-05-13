package com.github.coldab.shared.project;

import com.github.coldab.shared.edit.Edit;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class TextFile extends File {

  @OneToMany
  @Column
  private Map<Integer, Edit> edits = new HashMap<>();

  @OneToMany
  @Column
  private Map<Integer, Annotation> annotations = new HashMap<>();

  public TextFile(String path, LocalDateTime creationDate) {
    super(path, creationDate);
  }

  public Map<Integer, Edit> getEdits() {
    return edits;
  }

  public void setEdits(Map<Integer, Edit> revision) {
    edits = revision;
  }

  public Map<Integer, Annotation> getAnnotations() {
    return annotations;
  }
}
