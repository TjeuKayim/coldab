package com.github.coldab.client.project;

public interface TextFileObserver {

  void updateText(String text);

  void updateAnnotations();

  void updateTextFile();

  /**
   * An remote addition has been created.
   *
   * @param start index of the character to insert text after, or -1 if at the start of file
   * <p>
   * To insert an "," after "Hello" in "Hello World" to get "Hello, World", start = 4; text = ","
   * </p>
   */
  default void remoteAddition(int start, String text) {
  }

  /**
   * An remote deletion has been created.
   *
   * @param start the first character to delete, inclusive
   * @param length the number of characters to delete
   */
  default void remoteDeletion(int start, int length) {
  }
}
