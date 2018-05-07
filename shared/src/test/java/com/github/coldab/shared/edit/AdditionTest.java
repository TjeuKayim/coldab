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
    assertEquals("Letters schould be 'Hello'", letters, Arrays.asList(
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

  /**
   * Benchmark that creates 10 files with 1000 lines of "Lorem ipsum ...".
   */
  @Test(timeout = 1500)
  public void benchmarkLetter() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < 1000; i++) {
      stringBuilder.append("Lorem ipsum dolor sit amet, consectetuer adipiscing elit.");
    }
    String text = stringBuilder.toString();
    for (int i = 0; i < 10; i++) {
      new Addition(null, null, null, text);
    }
  }
}