package com.github.coldab.shared.ws;

import static org.junit.Assert.assertEquals;

import com.github.coldab.shared.TimeProvider;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.edit.Position;
import org.junit.Before;
import org.junit.Test;

public class ProjectServerTest {

  private Account account = new Account("Piet Hein", "piet@hein.email");

  @Before
  public void setUp() {
    TimeProvider.useMock();
  }

  @Test
  public void encodeAddition() {
    Addition expected =
        new Addition(3, account, new Position(4, 5), "Hello World");
    Addition actual = MessageEncoderTest.encodeDecode(expected);
    assertEquals(expected, actual);
    assertEquals(expected.getLetters(), actual.getLetters());
  }

  @Test
  public void encodeDeletion() {
    Deletion expected = new Deletion(3, account,
        new Position(4, 5),
        new Position(6, 7));
    Deletion actual = MessageEncoderTest.encodeDecode(expected);
    assertEquals(expected, actual);
  }
}