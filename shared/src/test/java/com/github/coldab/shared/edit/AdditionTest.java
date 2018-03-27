package com.github.coldab.shared.edit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdditionTest {
    @Test
    public void apply() {
        Addition a = new Addition(null, null, null, "Hello");
        List<Letter> letters = new ArrayList<>();
        a.apply(letters);
        assertEquals( "Letters schould be 'Hello'",letters, Arrays.asList(
                new Letter('H', 0),
                new Letter('e', 1),
                new Letter('l', 2),
                new Letter('l', 3),
                new Letter('o', 4)
        ));
    }

    @Test
    public void undo() {
    }
}