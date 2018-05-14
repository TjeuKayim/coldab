package com.github.coldab.client.project;

public interface TextFileController {

  void addObserver(TextFileState.Observer observer);

  void createAnnotation(int position, boolean todo, String text);

  void createAddition(int position, String text);

  void createDeletion(int position, int length);
}
