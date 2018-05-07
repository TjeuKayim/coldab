package com.github.coldab.client.project;

import com.github.coldab.client.gui.EditorController;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.TextFile;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TextFileService {
  private final TextFile file;
  private final Consumer<Edit> editSender;
  private final EditorController editorController;
  private final ObservableList<Edit> localState = FXCollections.observableArrayList();

  public TextFileService(TextFile file, Consumer<Edit> editSender,
      EditorController editorController) {
    this.file = file;
    this.editSender = editSender;
    this.editorController = editorController;
    //localState.addListener();
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

  public ObservableList<Edit> getLocalState() {
    return localState;
  }
}
