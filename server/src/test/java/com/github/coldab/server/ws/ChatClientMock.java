package com.github.coldab.server.ws;

import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.ws.ChatClient;
import java.util.concurrent.SynchronousQueue;

public class ChatClientMock implements ChatClient {

  public final SynchronousQueue<ChatMessage> messages = new SynchronousQueue<>();

  @Override
  public void message(ChatMessage message) {
    messages.offer(message);
  }
}
