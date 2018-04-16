package com.github.coldab.shared.project;

import com.github.coldab.shared.edit.Edit;
import java.time.LocalDateTime;
import java.util.Collection;

public class TextFile extends File {

  private Collection<Edit> edits;

  public TextFile(String path, LocalDateTime creationDate) {
    super(path, creationDate);
  }
}
