package com.github.coldab.shared.ws;

import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.TextFile;

public interface TextFileClient {

  /**
   * Send a new edit to the client.
   * @param edit the edit that gets send to the client.
   */
  void newEdit(Edit edit);

  /**
   * Send a updated file to the client.
   * @param updatedFile the updated file that gets send to the client.
   */
  void updateTextFile(TextFile updatedFile);

  /**
   * Send a edit to the client to confirm it.
   * @param edit the edit that is confirmed.
   */
  void confirmEdit(Edit edit);
}
