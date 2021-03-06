package com.github.coldab.shared.ws;

import static org.junit.Assert.assertEquals;

import com.github.coldab.shared.TimeProvider;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.ChatMessage;
import org.junit.Before;
import org.junit.Test;

public class ChatServerTest {

  private Account author = new Account("Piet Hein", "piet@hein.email", "1234");

  @Before
  public void setUp() {
    TimeProvider.useMock();
  }

  @Test
  public void encodeChatMessage() {
    ChatMessage expected = new ChatMessage("Hello World", author);
    ChatMessage actual = MessageEncoderTest.encodeDecode(expected);
    assertEquals(expected.getPostDate(), actual.getPostDate());
    assertEquals(expected.getAuthor(), actual.getAuthor());
    assertEquals(expected.getIndex(), actual.getIndex());
    assertEquals(expected.getText(), actual.getText());
  }
}