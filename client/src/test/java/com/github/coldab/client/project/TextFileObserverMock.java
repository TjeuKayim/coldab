package com.github.coldab.client.project;

public class TextFileObserverMock implements TextFileObserver {

  private String text = "";

  @Override
  public void updateText(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
