package com.github.coldab.shared.edit;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;


public class AdditionTest {

  private List<Letter> letters;

  @Before
  public void setUp() throws Exception {
    letters = new ArrayList<>();
  }

  @Test
  public void apply() {
    Addition addition = new Addition(null, null, null, "Hello");
    addition.apply(letters);
    EditTest.lettersEqual("Hello", letters);
  }

  @Test
  public void undo() {
    Addition a1 = new Addition(null, null, null, "Hello");
    a1.apply(letters);
    Letter position = letters.get(4);
    Addition a2 = new Addition(null, null, position, " World");
    a2.apply(letters);
    System.out.println(letters);
    EditTest.lettersEqual("Hello World", letters);
    a1.undo(letters);
    EditTest.lettersEqual(" World", letters);
  }

}