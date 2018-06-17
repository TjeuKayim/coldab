package com.github.coldab.shared.ws;

import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.TextFile;

public interface TextFileServer {

  void newEdit(Edit edit);

  void newAnnotation(Annotation annotation);

  void updateTextFile(TextFile updatedFile);
}
