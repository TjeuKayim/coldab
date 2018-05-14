package com.github.coldab.client.project;

import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.TextFile;

public interface TextFileClient {

  void newEdit(Edit edit);

  void newAnnotation(Annotation annotation);

  void updateTextFile(TextFile updatedFile);

  void confirmEdit(Edit edit);
}
