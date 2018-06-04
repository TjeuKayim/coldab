package com.github.coldab.shared.chat;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Chat {
  private final List<ChatMessage> messages = new LinkedList<>();
  private final List<ChatObserver> chatObservers = new LinkedList<>();

  public void addMessage(ChatMessage message) {
    messages.add(message);
    Collections.sort(messages);
    notifyObservers(message);
  }

  private void notifyObservers(ChatMessage message) {
    for (ChatObserver chatObserver : chatObservers) {
      chatObserver.receiveChatMessage(message);
    }
  }
  public void addObserver(ChatObserver chatObserver) {
    chatObservers.add(chatObserver);
  }

  public interface ChatObserver {
    void receiveChatMessage(ChatMessage message);
  }
}
