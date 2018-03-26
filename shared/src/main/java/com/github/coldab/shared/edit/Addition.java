package com.github.coldab.shared.edit;

import java.util.Collection;
import java.util.List;

/**
 * An Addition is an insertion of certain characters
 *
 * For or example an insertion of the characters "Hello World"
 */
public class Addition extends Edit {
    private Collection<Letter> letters;

    @Override
    public void apply(List<Letter> letters) {
        super.apply(letters);
        int index = -1;
        if (start != null) {
            index = letters.indexOf(start);
            if (index == -1) {
                throw new IllegalStateException();
            }
        }
        letters.addAll(index + 1, this.letters);
    }

    @Override
    public void undo(List<Letter> letters) {
        super.undo(letters);
        throw new UnsupportedOperationException();
    }
}
