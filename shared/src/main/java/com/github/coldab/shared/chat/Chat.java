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
    notifyObservers(message);
  }

  public List<ChatMessage> getMessages() {
    if (messages == null) return null;
    return Collections.unmodifiableList(messages);
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
