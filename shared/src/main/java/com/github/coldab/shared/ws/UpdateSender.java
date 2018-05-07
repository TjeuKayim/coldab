package com.github.coldab.shared.ws;

import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.session.Caret;
import java.util.List;

public interface UpdateSender {

  /**
   * New or updated files.
   */
  void files(List<TextFile> textFiles, List<BinaryFile> binaryFiles);

  void removeFile(int fileId);

  void edits(int fileId, List<Addition> additions, List<Deletion> deletions);

  void annotations(int fileId, List<Annotation> annotations);

  void caret(int fileId, Caret caret);
}
