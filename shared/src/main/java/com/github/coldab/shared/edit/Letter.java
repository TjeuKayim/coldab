package com.github.coldab.shared.edit;

import java.util.Objects;

public class Letter {
    private char character;
    private int position;

    public Letter(char c, int position) {
        this.character = c;
        this.position = position;
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
