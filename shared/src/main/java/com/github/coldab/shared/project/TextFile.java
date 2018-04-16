package com.github.coldab.shared.project;

import com.github.coldab.shared.edit.Edit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextFile extends File {

  private HashMap<Integer, Edit> edits = new HashMap<>();
  private final List<Observer> observers = new ArrayList<>();

  public TextFile(String path, LocalDateTime creationDate) {
    super(path, creationDate);
  }

  public void addEdit(Edit edit) {
    edits.put(edit.getIndex(), edit);
    notifyObservers();
  }

  public void setEdits(HashMap<Integer, Edit> revision) {
    edits = revision;
    notifyObservers();
  }

  private void notifyObservers() {
    for (Observer observer : observers) {
      observer.editsChanged();
    }
  }

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  public interface Observer {
    void editsChanged();
  }
}
