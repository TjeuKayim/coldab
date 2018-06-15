package com.github.coldab.shared.edit;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LetterTest {

  private Letter letter1;
  private Letter letter2;
  private Letter letter3;
  private char character;

  @Before
  public void setup() {
    character = 'w';
    letter1 = new Letter(1, 50, character);
    letter2 = new Letter(1, 50, character);
    letter3 = new Letter(1, 30, character);

  }

  @Test
  public void equals() {
    assertTrue("error in equals function,  false negative", letter1.equals(letter2));
    assertFalse("error in equals function, false positive", letter1.equals(letter3));
  }

}
