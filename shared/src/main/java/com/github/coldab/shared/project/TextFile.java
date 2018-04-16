package com.github.coldab.shared.project;

import com.github.coldab.shared.edit.Edit;
import java.time.LocalDateTime;
import java.util.HashMap;

public class TextFile extends File {

  private HashMap<Integer, Edit> edits = new HashMap<>();
  private HashMap<Integer, Annotation> annotations = new HashMap<>();

  public TextFile(String path, LocalDateTime creationDate) {
    super(path, creationDate);
  }

  public HashMap<Integer, Edit> getEdits() {
    return edits;
  }

  public void setEdits(HashMap<Integer, Edit> revision) {
    edits = revision;
  }
}
