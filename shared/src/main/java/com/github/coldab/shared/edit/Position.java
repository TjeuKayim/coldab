package com.github.coldab.shared.edit;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Position {

  private final int additionIndex;

  private final int index;

  public Position(int additionIndex, int index) {
    this.additionIndex = additionIndex;
    this.index = index;
  }

  public int getIndex() {
    return index;
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
        index == position1.index;
  }

  @Override
  public int hashCode() {
    return Objects.hash(additionIndex, index);
  }
}
