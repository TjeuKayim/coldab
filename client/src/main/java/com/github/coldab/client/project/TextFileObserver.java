package com.github.coldab.client.project;

import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.TextFile;

public interface TextFileObserver {

  void receiveEdit(Edit edit);

  void receiveAnnotation(Annotation annotation);

  void receiveUpdate(TextFile updatedFile);
}
