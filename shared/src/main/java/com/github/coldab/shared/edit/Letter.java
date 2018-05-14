package com.github.coldab.shared.edit;

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
}
