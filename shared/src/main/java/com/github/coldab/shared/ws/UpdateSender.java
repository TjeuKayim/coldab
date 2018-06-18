package com.github.coldab.shared.ws;

import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.TextFile;

public interface UpdateSender {

  /**
   * New or updated files.
   */
  void files(TextFile[] textFiles, BinaryFile[] binaryFiles);

  /**
   * remove a file.
   */
  void removeFile(int fileId);

}
