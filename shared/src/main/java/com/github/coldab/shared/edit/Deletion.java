package com.github.coldab.shared.edit;

import com.github.coldab.shared.account.Account;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A deletion is the removal of letters between a start (exclusive) and end (inclusive).
 */
@Entity
public class Deletion extends Edit {

  @Column(nullable = false)
  private final Letter end;

  /**
   * Create an deletion.
   *
   * @param start the start position (exclusive), or null if adding at the start of the document
   * @param end the end position (inclusive)
   */
  public Deletion(Account account, LocalDateTime creationDate, Letter start, Letter end) {
    super(account, creationDate, start);
    this.end = end;
  }
}
