package com.github.coldab.shared.ws;

import static com.github.coldab.shared.ws.MessageEncoderTest.encodeDecode;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.github.coldab.shared.TimeProvider;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.edit.Position;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.session.Caret;
import java.util.Collections;
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
    Addition actual = encodeDecode(expected);
    assertEquals(expected, actual);
    assertEquals(expected.getLetters(), actual.getLetters());
  }

  @Test
  public void encodeDeletion() {
    Deletion expected = new Deletion(3, account,
        new Position(4, 5),
        new Position(6, 7));
    Deletion actual = encodeDecode(expected);
    assertEquals(expected, actual);
  }

  @Test
  public void encodeBinaryFile() {
    BinaryFile expected = new BinaryFile(1, "/path/to/hello.world", "hash1234");
    BinaryFile actual = encodeDecode(expected);
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getHash(), actual.getHash());
    assertArrayEquals(expected.getPath(), actual.getPath());
  }

  @Test
  public void encodeTextFile() {
    TextFile expected = new TextFile(1, "/path/to/hello.world");
    expected.addEdit(new Addition(0, account, new Position(4, 5), "Hello World"));
    expected.getAnnotations().add(new Annotation(account, new Position(1, 2), true, "Hello World"));

    TextFile actual = encodeDecode(expected);
    assertEquals(expected.getId(), actual.getId());
    assertArrayEquals(expected.getPath(), actual.getPath());
    // Edits and annotations should not be serialized
    assertEquals(Collections.emptyList(), actual.getEdits());
    assertEquals(Collections.emptyList(), actual.getAnnotations());
  }

  @Test
  public void encodeAnnotation() {
    Annotation expected = new Annotation(account, new Position(1, 2), true, "Hello World");
    Annotation actual = encodeDecode(expected);
    assertEquals(expected.getAccount(), actual.getAccount());
    assertEquals(expected.getCreationDate(), actual.getCreationDate());
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getStart(), actual.getStart());
    assertEquals(expected.getText(), actual.getText());
    assertEquals(expected.isTodo(), actual.isTodo());
  }

  @Test
  public void encodeCaret() {
    Caret expected = new Caret(account, new Position(1, 2));
    Caret actual = encodeDecode(expected);
    assertEquals(expected, actual);
  }
}