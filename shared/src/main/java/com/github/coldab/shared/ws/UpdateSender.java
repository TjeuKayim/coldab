package com.github.coldab.shared.ws;

import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.session.Caret;
import java.util.List;

public interface UpdateSender {

  /**
   * New or updated files.
   */
  void files(TextFile[] textFiles, BinaryFile[] binaryFiles);

  void removeFile(int fileId);

  void annotations(int fileId, List<Annotation> annotations);

  void caret(int fileId, Caret caret);
}
