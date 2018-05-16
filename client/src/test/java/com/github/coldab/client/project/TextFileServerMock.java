package com.github.coldab.client.project;

import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.TextFile;

public class TextFileServerMock implements TextFileServer {

  private Edit edit;
  private Annotation annotation;
  private TextFile file;

  @Override
  public void newEdit(Edit edit) {
    this.edit = edit;
  }

  @Override
  public void newAnnotation(Annotation annotation) {
    this.annotation = annotation;
  }

  @Override
  public void updateTextFile(TextFile updatedFile) {
    file = updatedFile;
  }

  public Edit getEdit() {
    return edit;
  }

  public Annotation getAnnotation() {
    return annotation;
  }

  public TextFile getFile() {
    return file;
  }
}
