package com.github.coldab.shared.ws;

import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.TextFile;

public interface TextFileServer {


  /**
   * Send a new edit to the server.
   * @param edit the edit that gets send to the server.
   */
  void newEdit(Edit edit);

  /**
   * update a textfile on the server.
   * @param updatedFile the updated textfile that gets send to the server.
   */
  void updateTextFile(TextFile updatedFile);
}
