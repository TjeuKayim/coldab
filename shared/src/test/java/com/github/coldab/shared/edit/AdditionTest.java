package com.github.coldab.shared.edit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;


public class AdditionTest {

  @Test
  public void apply() {
    Addition a = new Addition(null, null, null, "Hello");
    List<Letter> letters = new ArrayList<>();
    a.apply(letters);
    assertEquals("Letters should be 'Hello'", letters, Arrays.asList(
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