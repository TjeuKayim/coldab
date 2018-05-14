package com.github.coldab.client.project;

import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.edit.Letter;
import com.github.coldab.shared.project.TextFile;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TextFileState {
  private final Queue<Edit> unconfirmedEdits = new ArrayDeque<>();
  private final List<Letter> letters = new ArrayList<>();
  private final TextFile file;
  private final List<Observer> observers = new ArrayList<>();

  public TextFileState(TextFile file) {
    this.file = file;
  }

  public void addRemoteEdit(Edit edit) {
    file.addEdit(edit);
    for (Edit unconfirmedEdit : unconfirmedEdits) {
      unconfirmedEdit.undo(letters);
    }
    edit.apply(letters);
    for (Edit unconfirmedEdit : unconfirmedEdits) {
      unconfirmedEdit.apply(letters);
    }
    notifyObservers();
  }

  /**
   * Local edits must be confirmed.
   */
  public void addLocalEdit(Edit edit) {
    edit.apply(letters);
    unconfirmedEdits.add(edit);
  }

  public void confirmLocalEdit(Edit edit) {
    unconfirmedEdits.remove();
    file.addEdit(edit);
  }

  public Letter letterAt(int index) {
    return letters.get(index);
  }

  private void notifyObservers() {
    String text = letters.stream()
        .map(l -> "" + l.getCharacter())
        .reduce((a, b) -> a + b)
        .orElse("");
    for (Observer observer : observers) {
      observer.updateText(text);
    }
  }

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  public interface Observer {
    void updateText(String text);
  }
}
