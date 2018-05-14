package com.github.coldab.shared.edit;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Letter {

  private final Addition addition;
  private char character;
  private int position;

  public Letter(Addition addition, char c, int position) {
    this.addition = addition;
    this.character = c;
    this.position = position;
  }

  @Override
  public String toString() {
    return String.format("%d:'%s'", position, character);
  }

  public char getCharacter() {
    return character;
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
    return character == letter.character
        && position == letter.position;
  }

  @Override
  public int hashCode() {

    return Objects.hash(character, position);
  }
}
