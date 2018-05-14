package com.github.coldab.shared.edit;

import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * In database saved as:
 * - additionId
 * - position
 */
@Embeddable
public class Letter {

  @Transient
  private final Addition addition;

  private final int position;

  public Letter(Addition addition, int position) {
    this.addition = addition;
    this.position = position;
  }

  @Override
  public String toString() {
    return String.format("%d:'%s'", position, getCharacter());
  }

  public char getCharacter() {
    return addition.getCharacter(position);
  }

  public int getPosition() {
    return position;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Letter letter = (Letter) o;
    return getCharacter() == ((Letter) o).getCharacter()
        && position == letter.position;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCharacter(), position);
  }
}
