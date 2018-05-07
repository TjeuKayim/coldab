package com.github.coldab.shared.chat;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Chat {
  private List<ChatMessage> messages;
  private List<ChatObserver> chatObservers;

  public Chat() {
    this.messages = new LinkedList<>();
    this.chatObservers = new LinkedList<>();
  }

  public void addMessage(ChatMessage message) {
    messages.add(message);
    Collections.sort(messages);
    notifyObservers();
  }

  public List<ChatMessage> getMessages() {
    if (messages == null) return null;
    return Collections.unmodifiableList(messages);
  }

  private void notifyObservers() {
    for (ChatObserver chatObserver : chatObservers) {
      chatObserver.chatUpdated(getMessages());
    }
  }
  public void addObserver(ChatObserver chatObserver) {
    chatObservers.add(chatObserver);
  }

  public interface ChatObserver {
    void chatUpdated(List<ChatMessage> messages);
  }
}
