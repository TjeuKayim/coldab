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
    new Addition(null, null, "Hello #remove this#World").apply(letters);
  }

  @Test
  public void apply() {
    Deletion deletion = new Deletion(null,
        letters.get(5).getPosition(),
        letters.get(18).getPosition());
    deletion.apply(letters);
    EditTestHelpers.lettersEqual("Hello World", letters);
  }

  @Test
  public void undo() {
    Deletion deletion = new Deletion(null,
        letters.get(5).getPosition(),
        letters.get(18).getPosition());
    deletion.apply(letters);
    EditTestHelpers.lettersEqual("Hello World", letters);
    deletion.undo(letters);
    EditTestHelpers.lettersEqual("Hello #remove this#World", letters);
  }

}