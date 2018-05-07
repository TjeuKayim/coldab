package com.github.coldab.shared.project;

import com.github.coldab.shared.edit.Edit;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class TextFile extends File {
  @OneToMany
  @Column
  private Collection<Annotation> annotations;
  @OneToMany
  @Column
  private Collection<Edit> edits;

  public TextFile(String path, LocalDateTime creationDate) {
    super(path, creationDate);
  }
}
