package com.github.coldab.shared.edit;

import com.github.coldab.shared.TimeProvider;
import com.github.coldab.shared.account.Account;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

  /**
   * The start position where the edit is applied.
   */
  @Embedded
  protected Position start;

  @ManyToOne
  private final Account account;

  /**
   * Create an edit.
   *
   * @param start the start position, or null if adding at the start of the document
   */
  public Edit(int index, Account account, Position start) {
    this.index = index;
    this.creationDate = TimeProvider.getInstance().now();
    this.start = start;
    this.account = account;
  }

  public Account getAccount() {
    return account;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Edit)) {
      return false;
    }
    Edit edit = (Edit) o;
    return id == edit.id &&
        index == edit.index &&
        Objects.equals(creationDate, edit.creationDate) &&
        Objects.equals(start, edit.start) &&
        Objects.equals(account, edit.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, index, creationDate, start, account);
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void confirmIndex(int index, Map<Integer, Integer> localIndices) {
    if (index >= 0) {
      throw new IllegalStateException("Index should be unconfirmed");
    }
    this.index = index;
    if (start.getAdditionIndex() < 0) {
      int startIndex = localIndices.get(start.getAdditionIndex());
      this.start = new Position(startIndex, start.getPosition());
    }
  }
}
