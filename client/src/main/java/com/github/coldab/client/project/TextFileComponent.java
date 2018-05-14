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
import java.util.logging.Logger;

public class TextFileComponent implements TextFileClient, TextFileController {

  private static final Logger LOGGER = Logger.getLogger(TextFileComponent.class.getName());

  private final TextFile file;
  private final Account account;
  private final TextFileServer server;
  private final EditorController editorController;
  private final TextFileState localState;

  public TextFileComponent(TextFile file, Account account,
      TextFileServer server, EditorController editorController) {
    this.file = file;
    this.account = account;
    this.server = server;
    this.editorController = editorController;
    localState = new TextFileState(file);
  }

  /**
   * Server notifies about new edit.
   */
  @Override
  public void newEdit(Edit edit) {
    localState.addRemoteEdit(edit);
  }

  @Override
  public void newAnnotation(Annotation annotation) {
    file.getAnnotations().add(annotation);
    editorController.showAnnotation(annotation);
  }

  @Override
  public void updateTextFile(TextFile updatedFile) {
    file.setPath(updatedFile.getPath());
  }

  @Override
  public void confirmEdit(Edit edit) {
    LOGGER.info("Edit confirmed");
    localState.confirmLocalEdit(edit);
  }

  @Override
  public void addObserver(TextFileState.Observer observer) {
    localState.addObserver(observer);
  }

  @Override
  public void createAnnotation(int position, boolean todo, String text) {
    Annotation annotation = new Annotation(account, now(), letterAt(position), todo, text);
    server.newAnnotation(annotation);
  }

  /**
   * Create a new addition and send it to the server.
   */
  @Override
  public void createAddition(int position, String text) {
    Addition addition = new Addition(account, now(), letterAt(position), text);
    createEdit(addition);
  }

  /**
   * Create a new deletion and send it to the server.
   *
   * @param position start (inclusive)
   * @param length amount of characters to remove
   */
  @Override
  public void createDeletion(int position, int length) {
    Letter start = letterAt(position);
    Letter end = letterAt(position + length);
    Deletion deletion = new Deletion(account, now(), start, end);
    createEdit(deletion);
  }

  private void createEdit(Edit edit) {
    localState.addLocalEdit(edit);
    server.newEdit(edit);
  }

  private Letter letterAt(int index) {
    return localState.letterAt(index);
  }

  private static LocalDateTime now() {
    return LocalDateTime.now(Clock.systemUTC());
  }
}
