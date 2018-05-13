package com.github.coldab.shared.edit;

import com.github.coldab.shared.account.Account;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * A deletion is the removal of letters between a start (exclusive) and end (inclusive).
 */
@Entity
public class Deletion extends Edit {

  @Transient
  private final Letter end;

  @Transient
  private final List<Letter> deletedLetters = new ArrayList<>();

  /**
   * Create an deletion.
   *
   * @param start the start position (exclusive), or null if adding at the start of the document
   * @param end the end position (inclusive)
   */
  public Deletion(Account account, LocalDateTime creationDate, Letter start, Letter end) {
    super(account, creationDate, start);
    if (end == null) {
      throw new IllegalArgumentException("End cannot be null");
    }
    this.end = end;
  }

  @Override
  public void apply(List<Letter> letters) {
    super.apply(letters);
    // Find start
    int startPosition = 0;
    if (start != null) {
      // add 1 because it's exclusive
      startPosition = indexOf(letters, start) + 1;
    }
    // Find end
    int endPosition = indexOf(letters, end);
    // Delete letters, but save deletedLetters
    deletedLetters.clear();
    for (int index = startPosition; index <= endPosition; index++) {
      deletedLetters.add(letters.get(startPosition));
      letters.remove(startPosition);
    }
  }

  @Override
  public void undo(List<Letter> letters) {
    super.undo(letters);
    int position = indexOf(letters, start);
    letters.addAll(position + 1, deletedLetters);
  }

  private int indexOf(List<Letter> letters, Letter letter) {
    int index = letters.indexOf(letter);
    if (index == -1) {
      throw new IllegalStateException();
    }
    return index;
  }
}
