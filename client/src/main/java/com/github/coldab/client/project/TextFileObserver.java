package com.github.coldab.client.project;

import java.util.Collection;

public interface TextFileObserver {

  void updateText(String text);

  /**
   * After resolving conflicts, the edits are flattened to RemoteDeletions and RemoteAdditions.
   */
  default void remoteEdits(Collection<RemoteDeletion> deletions, Collection<RemoteAddition> additions) {
  }

  /**
   * An remote addition, after resolving conflicts.
   * <p>
   * To insert an "," after "Hello" in "Hello World" to get "Hello, World", start = 4; text = ","
   * </p>
   */
  class RemoteAddition {

    /**
     * start index of the character to insert text after, or -1 if at the start of file.
     */
    private final int start;
    private final String text;

    public RemoteAddition(int start, String text) {
      this.start = start;
      this.text = text;
    }

    public int getStart() {
      return start;
    }

    public String getText() {
      return text;
    }
  }

  /**
   * An remote deletion, after resolving conflicts.
   */
  class RemoteDeletion {

    /**
     * The first character to delete, inclusive.
     */
    private final int start;
    /**
     * The number of characters to delete.
     */
    private final int length;

    public RemoteDeletion(int start, int length) {
      this.start = start;
      this.length = length;
    }

    public int getStart() {
      return start;
    }

    public int getLength() {
      return length;
    }
  }
}
