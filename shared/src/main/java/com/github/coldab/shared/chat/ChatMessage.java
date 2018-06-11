package com.github.coldab.shared.chat;

import com.github.coldab.shared.TimeProvider;
import com.github.coldab.shared.account.Account;
import com.google.gson.annotations.Expose;
import java.time.LocalDateTime;

public class ChatMessage implements Comparable<ChatMessage> {
  @Expose
  private LocalDateTime postDate;
  @Expose
  private int index;
  @Expose
  private String text;
  @Expose
  private Account author;

  public ChatMessage(String text, Account author) {
    this.postDate = TimeProvider.getInstance().now();
    this.text = text;
    this.author = author;
  }

  public LocalDateTime getPostDate() {
    return postDate;
  }

  public int getIndex() {
    return index;
  }

  public String getText() {
    return text;
  }

  public Account getAuthor() {
    return author;
  }

  @Override
  public int compareTo(ChatMessage o) {
    return getPostDate().compareTo(o.getPostDate());
  }

  @Override
  public String toString() {
    return author.getEmail() + ": " + text;
  }
}
