package com.github.coldab.shared.project;

import com.github.coldab.shared.edit.Edit;
import java.time.LocalDateTime;
import java.util.HashMap;

public class TextFile extends File {

  private HashMap<Integer, Edit> edits;

  public TextFile(String path, LocalDateTime creationDate) {
    super(path, creationDate);
  }
}
