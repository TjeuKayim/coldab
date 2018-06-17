package com.github.coldab.shared.edit;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Position {

  private final int additionIndex;

  private final int position;

  public Position(int additionIndex, int position) {
    this.additionIndex = additionIndex;
    this.position = position;
  }

  public int getPosition() {
    return position;
  }

  public int getAdditionIndex() {
    return additionIndex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Position position1 = (Position) o;
    return additionIndex == position1.additionIndex &&
        position == position1.position;
  }

  @Override
  public int hashCode() {
    return Objects.hash(additionIndex, position);
  }
}
