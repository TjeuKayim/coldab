package com.github.coldab.client.project;

import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.TextFile;
import java.util.function.Consumer;

public class FileService {
  private final TextFile file;
  private final Consumer<Edit> editSender;

  public FileService(TextFile file, Consumer<Edit> editSender) {
    this.file = file;
    this.editSender = editSender;
  }

  public void receiveUpdate(Edit edit) {

  }
}
