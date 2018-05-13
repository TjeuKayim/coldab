package com.github.coldab.shared.project;

import com.github.coldab.shared.edit.Edit;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class TextFile extends File {

  private Map<Integer, Edit> editsByIndex = new HashMap<>();

  private Map<Integer, Annotation> annotationsById = new HashMap<>();

  public TextFile(String path, LocalDateTime creationDate) {
    super(path, creationDate);
  }

  @OneToMany
  @Column
  public Collection<Edit> getEdits() {
    return editsByIndex.values();
  }

  public void setEdits(Collection<Edit> edits) {
    this.editsByIndex = edits.stream()
        .collect(Collectors.toMap(
            Edit::getIndex, Function.identity()
        ));
  }

  @OneToMany
  @Column
  public Collection<Annotation> getAnnotations() {
    return annotationsById.values();
  }

  public void setAnnotations(Collection<Annotation> edits) {
    this.annotationsById = edits.stream()
        .collect(Collectors.toMap(
            Annotation::getId, Function.identity()
        ));
  }

  public Map<Integer, Edit> getEditsByIndex() {
    return editsByIndex;
  }

  public Map<Integer, Annotation> getAnnotationsById() {
    return annotationsById;
  }
}
