package com.github.coldab.shared.project;

import com.github.coldab.shared.edit.Edit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class TextFile extends File {

  @OneToMany
  private transient List<Edit> edits = new ArrayList<>();

  @OneToMany
  private transient List<Annotation> annotations = new ArrayList<>();

  public TextFile(int id, String path) {
    super(id, path);
  }

  @Column
  public Collection<Edit> getEdits() {
    if (edits == null) {
      edits = new ArrayList<>();
    }
    return Collections.unmodifiableList(edits);
  }

  @Column
  public List<Annotation> getAnnotations() {
    if (annotations == null) {
      annotations = new ArrayList<>();
    }
    return annotations;
  }

  public void addEdit(Edit edit) {
    if (edits.size() != edit.getIndex()) {
      throw new IllegalStateException("Invalid index");
    }
    edits.add(edit);
  }
}
