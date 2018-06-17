package com.github.coldab.shared.edit;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.ws.MessageEncoder;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * An Addition is an insertion of certain characters.
 *
 * <p>For or example an insertion of the characters "Hello World".</p>
 */
@Entity
public class Addition extends Edit {

  @Transient
  private List<Letter> insertedLetters;

  @Expose
  @Column(length = 10485760)
  private String text;

  public Addition() {
  }

  /**
   * Create an addition.
   *
   * @param start the start position, or null if adding at the start of the document
   * @param text the characters to insert
   */
  public Addition(Account account, Position start, String text) {
    super(0, account, start);
    this.text = text;
  }

  public Addition(int index, Account account, Position start, String text) {
    super(index, account, start);
    this.text = text;
  }

  private void createLetters() {
    insertedLetters = new ArrayList<>();
    char[] charArray = text.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      insertedLetters.add(new Letter(getIndex(), i, charArray[i]));
    }
    // Lock modifications
    insertedLetters = Collections.unmodifiableList(insertedLetters);
  }

  public List<Letter> getLetters() {
    if (insertedLetters == null) {
      createLetters();
    }
    return insertedLetters;
  }

  @Override
  public void apply(List<Letter> letters) {
    try {
      int index = start == null ? -1 : indexOf(letters, start);
      letters.addAll(index + 1, getLetters());
    } catch (IllegalStateException e) {
      LOGGER.info("Resolve conflict by not applying this addition");
    }
  }

  @Override
  public void undo(List<Letter> letters) {
    letters.removeAll(getLetters());
  }

  char getCharacter(int position) {
    return text.charAt(position);
  }

  @Override
  public String toString() {
    return MessageEncoder.getGson().toJson(this);
  }

  public String getText() {
    return text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Addition)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Addition addition = (Addition) o;
    return Objects.equals(text, addition.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), text);
  }
}
