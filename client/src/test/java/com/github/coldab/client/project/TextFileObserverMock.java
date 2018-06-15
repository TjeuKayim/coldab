package com.github.coldab.client.project;

public class TextFileObserverMock implements TextFileObserver {

  private String text = "";
  private int updateTextFileCounter;

  @Override
  public void updateText(String text) {
    this.text = text;
  }

  @Override
  public void updateTextFile() {
    updateTextFileCounter++;
  }

  public String getText() {
    return text;
  }

  public int getUpdateTextFileCounter() {
    return updateTextFileCounter;
  }
}
