package com.github.coldab.shared.edit;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.ws.MessageEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * A deletion is the removal of letters between a start (exclusive) and end (inclusive).
 */
@Entity
public class Deletion extends Edit {

  private Position end;

  @Transient
  private transient List<Letter> deletedLetters = new ArrayList<>();

  /**
   * Create an deletion.
   *  @param start the start position (exclusive), or null if adding at the start of the document
   * @param end the end position (inclusive)
   */
  public Deletion(Account account, Position start, Position end) {
    super(0, account, start);
    if (end == null) {
      throw new IllegalArgumentException("End cannot be null");
    }
    this.end = end;
  }

  public Deletion(int index, Account account, Position start, Position end) {
    super(index, account, start);
    if (end == null) {
      throw new IllegalArgumentException("End cannot be null");
    }
    this.end = end;
  }

  private List<Letter> getDeletedLetters() {
    if (deletedLetters == null) {
      deletedLetters = new ArrayList<>();
    }
    return deletedLetters;
  }

  @Override
  public void apply(List<Letter> letters) {
    // Find start
    int startPosition = 0;
    if (start != null) {
      // add 1 because it's exclusive
      startPosition = indexOf(letters, start) + 1;
    }
    // Find end
    int endPosition = indexOf(letters, end);
    // Delete letters, but save deletedLetters
    getDeletedLetters().clear();
    for (int index = startPosition; index <= endPosition; index++) {
      getDeletedLetters().add(letters.get(startPosition));
      letters.remove(startPosition);
    }
  }

  @Override
  public void undo(List<Letter> letters) {
    int position = indexOf(letters, start);
    letters.addAll(position + 1, getDeletedLetters());
  }

  public Position getEnd() {
    return end;
  }

  private int indexOf(List<Letter> letters, Position position) {
    return IntStream.range(0, letters.size())
        .filter(i -> letters.get(i).getPosition().equals(position))
        .findAny().orElseThrow(IllegalStateException::new);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Deletion)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Deletion deletion = (Deletion) o;
    return Objects.equals(end, deletion.end);
  }

  @Override
  public void confirmIndex(int index, Map<Integer, Integer> localIndices) {
    super.confirmIndex(index, localIndices);
    if (end.getAdditionIndex() < 0) {
      int endIndex = localIndices.get(end.getAdditionIndex());
      this.end = new Position(endIndex, end.getPosition());
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), end);
  }

  @Override
  public String toString() {
    return MessageEncoder.getGson().toJson(this);
  }
}
