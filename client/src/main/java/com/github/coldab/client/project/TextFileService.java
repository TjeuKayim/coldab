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
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class TextFileService implements TextFileObserver {

  private final TextFile file;
  private final Account account;
  private final TextFileObserver server;
  private final EditorController editorController;
  private final List<Edit> localStateEdits = new ArrayList<>();
  private final List<Letter> localStateLetters = new ArrayList<>();
  private static final Logger LOGGER = Logger.getLogger(TextFileService.class.getName());

  public TextFileService(TextFile file, Account account,
      TextFileObserver server, EditorController editorController) {
    this.file = file;
    this.account = account;
    this.server = server;
    this.editorController = editorController;
  }

  /**
   * Server notifies about new edit.
   */
  @Override
  public void newEdit(Edit edit) {
    file.getEdits().put(edit.getIndex(), edit);
  }

  @Override
  public void newAnnotation(Annotation annotation) {
    file.getAnnotations().put(annotation.getId(), annotation);
    editorController.showAnnotation(annotation);
  }

  @Override
  public void updateTextFile(TextFile updatedFile) {
    file.setPath(updatedFile.getPath());
  }

  /**
   * Create a new addition and send it to the server.
   */
  public void createAddition(int position, String text) {
    Addition addition = new Addition(account, now(), letterAt(position), text);
    createEdit(addition);
  }

  public void createAnnotation(int position, boolean todo, String text) {
    Annotation annotation = new Annotation(account, now(), letterAt(position), todo, text,
        Collections.emptyList());
    server.newAnnotation(annotation);
  }

  public void confirmEdit(Edit edit) {
    LOGGER.fine("confirmed edit");
  }

  private static LocalDateTime now() {
    return LocalDateTime.now(Clock.systemUTC());
  }

  /**
   * Create a new deletion and send it to the server.
   *
   * @param position start (inclusive)
   * @param length amount of characters to remove
   */
  public void createDeletion(int position, int length) {
    Letter start = letterAt(position);
    Letter end = letterAt(position + length);
    Deletion deletion = new Deletion(account, now(), start, end);
    createEdit(deletion);
  }

  private void createEdit(Edit edit) {
    localStateEdits.add(edit);
    edit.apply(localStateLetters);
    server.newEdit(edit);
  }

  private Letter letterAt(int index) {
    return localStateLetters.get(index);
  }
}
