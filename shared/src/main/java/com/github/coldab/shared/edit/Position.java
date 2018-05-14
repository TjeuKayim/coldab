package com.github.coldab.shared.edit;

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
}
