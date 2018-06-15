package com.github.coldab.client.project;

import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.ws.TextFileServer;

public class TextFileServerMock implements TextFileServer {

  private Edit edit;
  private TextFile file;

  @Override
  public void newEdit(Edit edit) {
    this.edit = edit;
  }

  @Override
  public void updateTextFile(TextFile updatedFile) {
    file = updatedFile;
  }

  public Edit getEdit() {
    return edit;
  }

  public TextFile getFile() {
    return file;
  }
}
