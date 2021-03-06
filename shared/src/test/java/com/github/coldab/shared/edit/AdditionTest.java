package com.github.coldab.shared.edit;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;


public class AdditionTest {

  private List<Letter> letters;

  @Before
  public void setUp() {
    letters = new ArrayList<>();
  }

  @Test
  public void apply() {
    Addition addition = new Addition(null, null, "Hello");
    addition.apply(letters);
    EditTestHelpers.lettersEqual("Hello", letters);
  }

  @Test
  public void undo() {
    Addition a1 = new Addition(null, null, "Hello");
    a1.apply(letters);
    Position position = letters.get(4).getPosition();
    Addition a2 = new Addition(null, position, " World");
    a2.apply(letters);
    System.out.println(letters);
    EditTestHelpers.lettersEqual("Hello World", letters);
    a1.undo(letters);
    EditTestHelpers.lettersEqual(" World", letters);
  }

}