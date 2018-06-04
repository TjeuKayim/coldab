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

  /**
   * remove a file.
   * @param fileId the id of the file that gets removed.
   */
  void removeFile(int fileId);

  /**
   * Add new or updated annotations to a file.
   * @param fileId the id of the file.
   * @param annotations the list of new or updated annotations.
   */

  void annotations(int fileId, List<Annotation> annotations);

  /**
   * Add a carret new or update carret to a file.
   * @param fileId the id of the file.
   * @param caret the new or updated caret.
   */

  void caret(int fileId, Caret caret);
}
