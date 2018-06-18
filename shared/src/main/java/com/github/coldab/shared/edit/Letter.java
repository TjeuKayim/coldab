package com.github.coldab.shared.edit;

import java.util.Objects;

public class Letter {

  private final Position position;
  private final char character;

  public Letter(Position position, char character) {
    this.position = position;
    this.character = character;
  }

  public Letter(int additionIndex, int position, char character) {
    this.position = new Position(additionIndex, position);
    this.character = character;
  }

  public Position getPosition() {
    return position;
  }

  public char getCharacter() {
    return character;
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
    return character == letter.character &&
        Objects.equals(position, letter.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(position, character);
  }
}
