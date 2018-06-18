package com.github.coldab.client.project;

public interface TextFileController {

  void addObserver(TextFileObserver observer);

  /**
   * Create a new addition and send it to the server.
   * @param position index of the character to insert text after, or -1 if at the start of file
   */
  void createAddition(int position, String text);

  /**
   * Create a new deletion and send it to the server.
   *
   * @param position start (inclusive)
   * @param length amount of characters to remove
   */
  void createDeletion(int position, int length);
}
