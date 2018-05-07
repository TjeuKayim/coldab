package com.github.coldab.client.project;

import com.github.coldab.client.gui.EditorController;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.edit.Letter;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.TextFile;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TextFileService {
  private final TextFile file;
  private final Account account;
  private final Consumer<Edit> editSender;
  private final EditorController editorController;
  private final List<Edit> localState = new ArrayList<>();

  public TextFileService(TextFile file, Account account, Consumer<Edit> editSender,
      EditorController editorController) {
    this.file = file;
    this.account = account;
    this.editSender = editSender;
    this.editorController = editorController;
  }

  /**
   * Receive an update from the server.
   */
  public void receiveEdit(Edit edit) {
    file.getEdits().put(edit.getIndex(), edit);
    // Try to add the edit

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
   * Create a new addition and send it to the server.
   */
  public void createAddition(int position, String text) {
    Addition addition = new Addition(account, now(), letterAt(position), text);
    localState.add(addition);
    editSender.accept(addition);
  }

  private static LocalDateTime now() {
    return LocalDateTime.now(Clock.systemUTC());
  }

  /**
   * Create a new deletion and send it to the server.
   * @param position start (inclusive)
   * @param length amount of characters to remove
   */
  public void createDeletion(int position, int length) {
    Letter start = letterAt(position);
    Letter end = letterAt(position + length);
    Deletion deletion = new Deletion(account, now(), start, end);
    localState.add(deletion);
    editSender.accept(deletion);
  }

  private Letter letterAt(int index) {
    return null;
  }
}
