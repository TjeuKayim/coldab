package com.github.coldab.client.project;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.Chat;
import com.github.coldab.shared.chat.Chat.ChatObserver;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.ws.ChatServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ChatComponentTest {

  private Account account = new Account("TestAccount", "TestMail@test.com", "1234");
  private ChatComponent chatComponent;
  private ChatServer chatServer;
  private Chat chat;
  private ChatObserver chatObserver;

  @Before
  public void setUp() {
    chatServer = Mockito.mock(ChatServer.class);
    chatObserver = Mockito.mock(ChatObserver.class);
    chat = new Chat();
    chat.addObserver(chatObserver);
    chatComponent = new ChatComponent(chat, chatServer);
  }

  @Test
  public void message() {
    ChatMessage message = new ChatMessage("Test", account);
    chatComponent.message(message);
    Mockito.verify(chatObserver).receiveChatMessage(message);
  }

  @Test
  public void sendMessage() {
    ChatMessage message = new ChatMessage("Test", account);
    chatComponent.sendMessage(message);
    Mockito.verify(chatServer).message(message);
  }
}