package com.github.coldab.shared.edit;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class DeletionTest {

  private List<Letter> letters;

  @Before
  public void setUp() throws Exception {
    letters = new ArrayList<>();
  }

  @Test
  public void apply() {
    new Addition(null, null, null, "Hello #remove this#World").apply(letters);
    System.out.println(letters);
    EditTest.lettersEqual("Hello #remove this#World", letters);
    Deletion deletion = new Deletion(null, null, letters.get(5), letters.get(18));
    deletion.apply(letters);
    EditTest.lettersEqual("Hello World", letters);
  }

  @Test
  public void undo() {
  }

}