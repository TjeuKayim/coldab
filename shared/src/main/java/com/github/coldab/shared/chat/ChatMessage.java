package com.github.coldab.shared.chat;

import com.github.coldab.shared.account.Account;
import java.util.Date;

public class ChatMessage implements Comparable<ChatMessage> {
  private int id;
  private Date postDate;
  private String text;
  private Account author;

  public ChatMessage(String text, Account author) {
    this.postDate = new Date();
    this.text = text;
    this.author = author;
  }

  public Date getPostDate() {
    return postDate;
  }

  public int getId() {
    return id;
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
    return author.getNickName() + ": " + text;
  }
}
