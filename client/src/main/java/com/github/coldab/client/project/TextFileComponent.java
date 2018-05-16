package com.github.coldab.client.project;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.edit.Position;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.TextFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TextFileComponent implements TextFileClient, TextFileController {

  private static final Logger LOGGER = Logger.getLogger(TextFileComponent.class.getName());

  private final TextFile file;
  private final Account account;
  private final TextFileServer server;
  private final TextFileState localState;
  private final List<TextFileObserver> observers = new ArrayList<>();
  private int editCounter;

  public TextFileComponent(TextFile file, Account account,
      TextFileServer server) {
    this.file = file;
    this.account = account;
    this.server = server;
    localState = new TextFileState(file, observers);
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
    observers.forEach(TextFileObserver::updateAnnotations);
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
  public void addObserver(TextFileObserver observer) {
    observers.add(observer);
  }

  @Override
  public void createAnnotation(int position, boolean todo, String text) {
    Annotation annotation = new Annotation(account, getPosition(position), todo, text);
    server.newAnnotation(annotation);
  }

  /**
   * Create a new addition and send it to the server.
   * @param position index of the character to insert text after, or -1 if at the start of file
   */
  @Override
  public void createAddition(int position, String text) {
    int index = getIndex();
    Addition addition = new Addition(index, account, getPosition(position), text);
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
    Position start = getPosition(position);
    Position end = getPosition(position + length);
    int index = getIndex();
    Deletion deletion = new Deletion(index, account, start, end);
    createEdit(deletion);
  }

  private int getIndex() {
    editCounter++;
    return -editCounter;
  }

  private void createEdit(Edit edit) {
    localState.addLocalEdit(edit);
    server.newEdit(edit);
  }

  private Position getPosition(int index) {
    if (index == -1) {
      return null;
    }
    return localState.letterAt(index).getPosition();
  }

}
