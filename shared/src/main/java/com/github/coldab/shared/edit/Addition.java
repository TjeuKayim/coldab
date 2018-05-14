package com.github.coldab.shared.edit;

import com.github.coldab.shared.account.Account;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

  private String text;

  /**
   * Create an addition.
   *  @param start the start position, or null if adding at the start of the document
   * @param text the characters to insert
   */
  public Addition(Account account, Letter start, String text) {
    super(0, account, start);
    this.text = text;
    createLetters();
  }

  public Addition(int index, Account account, Letter start, String text) {
    super(index, account, start);
    this.text = text;
    createLetters();
  }

  private void createLetters() {
    insertedLetters = new ArrayList<>();
    char[] charArray = text.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      insertedLetters.add(new Letter(this, i));
    }
    // Lock modifications
    insertedLetters = Collections.unmodifiableList(insertedLetters);
  }

  public List<Letter> getLetters() {
    return insertedLetters;
  }

  @Override
  public void apply(List<Letter> letters) {
    int index = -1;
    if (start != null) {
      index = letters.indexOf(start);
      if (index == -1) {
        throw new IllegalStateException();
      }
    }
    letters.addAll(index + 1, insertedLetters);
  }

  @Override
  public void undo(List<Letter> letters) {
    letters.removeAll(insertedLetters);
  }

  char getCharacter(int position) {
    return text.charAt(position);
  }
}
