package com.github.coldab.client.project;

import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.edit.Letter;
import com.github.coldab.shared.edit.Position;
import com.github.coldab.shared.project.TextFile;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

public class TextFileState {
  private final Queue<Edit> unconfirmedEdits = new ArrayDeque<>();
  private final List<Letter> letters = new ArrayList<>();
  private final TextFile file;
  private final List<TextFileObserver> observers;

  public TextFileState(TextFile file,
      List<TextFileObserver> observers) {
    this.file = file;
    this.observers = observers;
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
    notifyObservers(edit);
  }

  /**
   * Local edits must be confirmed.
   */
  public void addLocalEdit(Edit edit) {
    edit.apply(letters);
    unconfirmedEdits.add(edit);
  }

  public void confirmLocalEdit(Edit edit) {
    int unconfirmed = unconfirmedEdits.remove().getIndex();
    file.addEdit(edit);
    letters.stream()
        .map(Letter::getPosition)
        .filter(p -> p.getAdditionIndex() == unconfirmed)
        .forEach(p -> p.confirmAddition(edit.getIndex()));
  }

  public Letter letterAt(int index) {
    return letters.get(index);
  }

  private void notifyObservers(Edit edit) {
    String text = letters.stream()
        .map(l -> "" + l.getCharacter())
        .reduce((a, b) -> a + b)
        .orElse("");
    for (TextFileObserver observer : observers) {
      observer.updateText(text);
      //parseEdits(edit, observer);
    }
  }

  private void parseEdits(Edit edit, TextFileObserver observer) {
    if (edit instanceof Addition) {
      Addition addition = (Addition) edit;
      Letter firstLetter = addition.getLetters().get(0);
      int start;
      if (firstLetter == null) {
        start = -1;
      } else {
        start = letters.indexOf(firstLetter);
      }
      observer.remoteAddition(start, addition.getText());
    } else if (edit instanceof Deletion) {
      Deletion deletion = (Deletion) edit;
      Position startPosition = deletion.getStart();
      int start = startPosition == null ? -1 : indexOf(startPosition);
      int length = indexOf(deletion.getEnd()) - start;
      observer.remoteDeletion(start, length);
    }
  }

  private int indexOf(Position startPosition) {
    return IntStream.range(0, letters.size())
        .filter(i -> startPosition.equals(letters.get(i).getPosition()))
        .findAny().orElseThrow(IllegalStateException::new);
  }
}
