package com.github.coldab.shared.edit;

import com.github.coldab.shared.account.Account;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 * An Edit is a change in a {@link com.github.coldab.shared.project.TextFile}.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Edit {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @Column(nullable = false)
  private int index;

  @Column(nullable = false)
  private final LocalDateTime creationDate;

  /** The start position where the edit is applied. */
  @Embedded
  protected final Letter start;

  @ManyToOne
  private final Account account;

  /**
   * Create an edit.
   *
   * @param start the start position, or null if adding at the start of the document
   */
  public Edit(int index, Account account, Letter start) {
    this.creationDate = LocalDateTime.now(Clock.systemUTC());
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
  public abstract void apply(List<Letter> letters);

  /**
   * Undo this edit.
   *
   * @param letters the letters to undo changes on
   */
  public abstract void undo(List<Letter> letters);
}
