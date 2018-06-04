package com.github.coldab.client.project;

public class TextFileObserverMock implements TextFileObserver {
  private String text = "";
  private int updateAnnotationsCounter;
  private int updateTextFileCounter;

  @Override
  public void updateText(String text) {
    this.text = text;
  }

  @Override
  public void updateAnnotations() {
    updateAnnotationsCounter++;
  }

  @Override
  public void updateTextFile() {
    updateTextFileCounter++;
  }

  public String getText() {
    return text;
  }

  public int getUpdateAnnotationsCounter() {
    return updateAnnotationsCounter;
  }

  public int getUpdateTextFileCounter() {
    return updateTextFileCounter;
  }
}
