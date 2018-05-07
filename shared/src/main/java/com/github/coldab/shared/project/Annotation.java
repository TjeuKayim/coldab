package com.github.coldab.shared.project;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Letter;
import java.time.LocalDateTime;

public class Annotation {
  private int id;
  private final boolean todo;
  private final String text;
  private final Account account;
  private final LocalDateTime creationDate;
  private final Letter start;

  public Annotation(Account account, LocalDateTime creationDate,
      Letter start, boolean todo, String text) {
    this.account = account;
    this.creationDate = creationDate;
    this.start = start;
    this.todo = todo;
    this.text = text;
  }

  public boolean isTodo() {
    return todo;
  }

  public int getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public Account getAccount() {
    return account;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public Letter getStart() {
    return start;
  }
}
