package com.github.coldab.client.project;

import com.github.coldab.client.gui.EditorController;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.edit.Letter;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.TextFile;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TextFileService {
  private final TextFile file;
  private final Consumer<Edit> editSender;
  private final EditorController editorController;
  private final List<Edit> localState = new ArrayList<>();

  public TextFileService(TextFile file, Consumer<Edit> editSender,
      EditorController editorController) {
    this.file = file;
    this.editSender = editSender;
    this.editorController = editorController;
  }

  /**
   * Receive an update from the server.
   */
  public void receiveEdit(Edit edit) {

  }

  public void receiveAnnotation(Annotation annotation) {
    throw new UnsupportedOperationException();
  }

  /**
   * Receive update about file, like changing the filename.
   */
  public void receiveUpdate(TextFile updatedFile) {
    file.setPath(updatedFile.getPath());
  }

  public void addObserver(Consumer<String> observer) {

  }

  /**
   * Send it to the server.
   */
  public void saveEdit(Edit edit) {

  }

  /**
   * Create a new deletion and send it to the server.
   * @param position start (inclusive)
   * @param length amount of characters to remove
   */
  public void createDeletion(int position, int length) {

  }

  private Letter letterAt(int index) {
    return null;
  }
}
