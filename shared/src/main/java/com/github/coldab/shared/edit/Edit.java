package com.github.coldab.shared.edit;

import com.github.coldab.shared.account.Account;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * An Edit is a change in a {@link com.github.coldab.shared.project.TextFile}.
 */
@MappedSuperclass
public abstract class Edit {
  private int index;

  @Column(nullable = false)
  private final LocalDateTime creationDate;

  /** The start position where the edit is applied. */
  @Column(nullable = false)
  protected final Letter start;

  @Column(nullable = false)
  private final Account account;

  @Column(nullable = false)
  private boolean applied = false;

  /**
   * Create an edit.
   *
   * @param start the start position, or null if adding at the start of the document
   */
  public Edit(Account account, LocalDateTime creationDate, Letter start) {
    this.creationDate = creationDate;
    this.start = start;
    this.account = account;
  }

  public int getIndex() {
    return index;
  }

  /**
   * Apply this edit.
   *
   * @param letters the letters to apply changes on
   */
  public void apply(List<Letter> letters) {
    if (applied) {
      throw new IllegalStateException();
    }
    applied = true;
  }

  /**
   * Undo this edit.
   *
   * @param letters the letters to undo changes on
   */
  public void undo(List<Letter> letters) {
    if (!applied) {
      throw new IllegalStateException();
    }
    applied = false;
  }
}
