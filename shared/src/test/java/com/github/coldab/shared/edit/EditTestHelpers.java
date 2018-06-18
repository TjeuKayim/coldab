package com.github.coldab.shared.edit;

import static org.junit.Assert.assertEquals;

import java.util.List;

public class EditTestHelpers {

  public static void lettersEqual(String expected, List<Letter> letters) {
    String actual = letters.stream()
        .map(l -> "" + l.getCharacter())
        .reduce((a, b) -> a + b)
        .orElse(null);
    System.out.printf("Compare:\n%s\n%s\n", expected, actual);
    char[] charArray = expected.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      assertEquals(charArray[i], letters.get(i).getCharacter());
    }
  }
}
