package com.github.coldab.client.project;

import com.github.coldab.client.project.TextFileObserver.RemoteAddition;
import com.github.coldab.client.project.TextFileObserver.RemoteDeletion;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.edit.Letter;
import com.github.coldab.shared.edit.Position;
import com.github.coldab.shared.project.TextFile;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.stream.Collectors;
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
    List<Letter> previous = new ArrayList<>(this.letters);
    file.addEdit(edit);
    for (Edit unconfirmedEdit : unconfirmedEdits) {
      unconfirmedEdit.undo(this.letters);
    }
    edit.apply(this.letters);
    for (Edit unconfirmedEdit : unconfirmedEdits) {
      unconfirmedEdit.apply(this.letters);
    }
    parseEdits(previous);
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
    String text = lettersToString(letters);
    for (TextFileObserver observer : observers) {
      observer.updateText(text);
    }
  }

  private void parseEdits(List<Letter> beforeEdits) {
    // Deleted letters
    LinkedHashMap<Integer, Letter> deletedLetters = new LinkedHashMap<>();
    for (int i = 0; i < beforeEdits.size(); i++) {
      Letter letter = beforeEdits.get(i);
      if (!letters.contains(letter)) {
        deletedLetters.put(i, letter);
      }
    }
    ArrayList<RemoteDeletion> deletions = new ArrayList<>();
    for (List<Letter> deletion : groupNeighbours(deletedLetters)) {
      int position = beforeEdits.indexOf(deletion.get(0)) - 1;
      int length = deletion.size();
      deletions.add(new RemoteDeletion(position, length));
    }
    // Added letters
    LinkedHashMap<Integer, Letter> addedLetters = new LinkedHashMap<>();
    for (int i = 0; i < letters.size(); i++) {
      Letter letter = letters.get(i);
      if (!beforeEdits.contains(letter)) {
        addedLetters.put(i, letter);
      }
    }
    ArrayList<RemoteAddition> additions = new ArrayList<>();
    for (List<Letter> addition : groupNeighbours(addedLetters)) {
      int index = letters.indexOf(addition.get(0));
      int position;
      if (index == 0) {
        position = -1;
      } else {
        Letter previousLetter = letters.get(index - 1);
        position = beforeEdits.indexOf(previousLetter);
      }
      String text = lettersToString(addition);
      additions.add(new RemoteAddition(position, text));
    }
    observers.forEach(o -> o.remoteEdits(deletions, additions));
  }

  private int indexOf(Position startPosition) {
    return IntStream.range(0, letters.size())
        .filter(i -> startPosition.equals(letters.get(i).getPosition()))
        .findAny().orElseThrow(IllegalStateException::new);
  }

  private <T> List<List<T>> groupNeighbours(LinkedHashMap<Integer, T> list) {
    ArrayList<List<T>> output = new ArrayList<>();
    ArrayList<T> group = null;
    int previous = -2;
    for (Entry<Integer, T> current : list.entrySet()) {
      if (current.getKey() != previous + 1) {
        group = new ArrayList<>();
        output.add(group);
      }
      group.add(current.getValue());
      previous = current.getKey();
    }
    return output;
  }

  private String lettersToString(Collection<Letter> letters) {
    return letters.stream()
        .map(letter -> String.valueOf(letter.getCharacter()))
        .collect(Collectors.joining());
  }
}
