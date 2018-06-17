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
  private List<Edit> edits = new ArrayList<>();

  @OneToMany
  private List<Annotation> annotations = new ArrayList<>();

  public TextFile(String path) {
    super(path);
  }

  @Column
  public Collection<Edit> getEdits() {
    return Collections.unmodifiableList(edits);
  }

  @Column
  public List<Annotation> getAnnotations() {
    return annotations;
  }

  public void addEdit(Edit edit) {
    if (edits.size() != edit.getIndex()) {
      throw new IllegalStateException("Invalid index");
    }
    edits.add(edit);
  }
}
