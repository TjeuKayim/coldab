package com.github.coldab.client.project;

public interface TextFileObserver {

  void updateText(String text);

  void updateAnnotations();

  void updateTextFile();
}
