package com.github.coldab.shared.project;

import com.github.coldab.shared.edit.Edit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class TextFile extends File {

  @OneToMany(cascade = CascadeType.ALL)
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Edit> edits = new ArrayList<>();

  @Transient
  private List<Annotation> annotations = new ArrayList<>();

  public TextFile() {
  }

  public TextFile(int id, String path) {
    super(id, path);
  }

  public List<Edit> getEdits() {
    if (edits == null) {
      edits = new ArrayList<>();
    }
    return Collections.unmodifiableList(edits);
  }

  public List<Annotation> getAnnotations() {
    if (annotations == null) {
      annotations = new ArrayList<>();
    }
    return annotations;
  }

  public void addEdit(Edit edit) {
    int expectedIndex = getEdits().size();
    int actualIndex = edit.getIndex();
    if (expectedIndex != actualIndex) {
      throw new IllegalStateException(
          String.format("Invalid index (expected %d, got %d", expectedIndex, actualIndex));
    }
    edits.add(edit);
  }

  /**
   * Confirms an edit and saves it.
   *
   * @param localIndices Maps local-indices to remote-indices
   */
  public void confirmEdit(Edit edit, Map<Integer, Integer> localIndices) {
    edit.confirmIndex(edits.size(), localIndices);
    addEdit(edit);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TextFile)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    TextFile textFile = (TextFile) o;
    return Objects.equals(edits, textFile.edits) &&
        Objects.equals(annotations, textFile.annotations);
  }

  @Override
  public int hashCode() {

    return Objects.hash(super.hashCode(), edits, annotations);
  }

  public void reset() {
    edits.clear();
  }
}
