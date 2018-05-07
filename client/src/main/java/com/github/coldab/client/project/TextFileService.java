package com.github.coldab.client.project;

import com.github.coldab.client.gui.EditorController;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.edit.Letter;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.TextFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class TextFileService {
  private final TextFile file;
  private final Consumer<Edit> editSender;
  private final EditorController editorController;
  private final List<Consumer<String>> localStateObservers = new ArrayList<>();
  private String localState = "";

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

  public void addObserver(Consumer<String> observer) {

  }

  public String getLocalState() {
    return localState;
  }

  public void setLocalState(String localState) {
    this.localState = localState;
  }

  /**
   * Compares the previous text with the new text.
   * @return addition, deletion, edit (deletion & addition) or empty list
   */
  private List<Edit> findEdit(String previous, String current) {
    if (previous.equals(current)) {
      // They are equal
      return Collections.emptyList();
    } else if (current.length() > previous.length()) {
      // addition or change
      return findAddition(previous, current);
    } else {
      // deletion or change
      return null;
    }
  }

  private List<Edit> findAddition(String previous, String current) {
    for (int i = 0; i < previous.length(); i++) {
      char oldChar = previous.charAt(i);
      char newChar = current.charAt(i);
      if (oldChar != newChar) {
        // Found difference
        int equalCharactersFromEnd = 0;
        while (fromEnd(previous, i) == fromEnd(current, i)) {
          equalCharactersFromEnd++;
        }
        String addedText = current.substring(i, current.length() - equalCharactersFromEnd);
        Letter position = letterAt(i);
        Addition addition = new Addition(null, null, position, addedText);
        // Is pure addition?
        if (i + equalCharactersFromEnd == previous.length()) {
          return Collections.singletonList(addition);
        } else {

          Deletion deletion = new Deletion(null, null, null, null);
        }
      }
    }
    return null;
  }
  
  private char fromEnd(String str, int index) {
    int i = str.length() - 1 - index;
    return str.charAt(i);
  }

  private Letter letterAt(int index) {
    return null;
  }
}
