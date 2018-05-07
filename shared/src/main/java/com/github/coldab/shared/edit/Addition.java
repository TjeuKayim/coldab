package com.github.coldab.shared.edit;

import com.github.coldab.shared.account.Account;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * An Addition is an insertion of certain characters.
 *
 * <p>For or example an insertion of the characters "Hello World".</p>
 */
@Entity
public class Addition extends Edit {

  @Column(nullable = false)
  private Collection<Letter> letters;

  /**
   * Create an addition.
   *
   * @param start the start position, or null if adding at the start of the document
   * @param text the characters to insert
   */
  public Addition(Account account, LocalDateTime creationDate, Letter start, String text) {
    super(account, creationDate, start);
    letters = new ArrayList<>();
    char[] charArray = text.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      letters.add(new Letter(charArray[i], i));
    }
  }


  @Override
  public void apply(List<Letter> letters) {
    super.apply(letters);
    int index = -1;
    if (start != null) {
      index = letters.indexOf(start);
      if (index == -1) {
        throw new IllegalStateException();
      }
    }
    letters.addAll(index + 1, this.letters);
  }

  @Override
  public void undo(List<Letter> letters) {
    super.undo(letters);
    throw new UnsupportedOperationException();
  }
}
