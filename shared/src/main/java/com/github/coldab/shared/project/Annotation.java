package com.github.coldab.shared.project;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Letter;
import java.time.LocalDateTime;
import java.util.List;

public class Annotation {

  private final boolean todo;
  private final String text;
  private final Account account;
  private final LocalDateTime creationDate;
  private final Letter start;


  private final List<Account> mentioned;
  private int id;

  public Annotation(Account account, LocalDateTime creationDate,
      Letter start, boolean todo, String text, List<Account> mentioned) {
    this.account = account;
    this.creationDate = creationDate;
    this.start = start;
    this.todo = todo;
    this.text = text;
    this.mentioned = mentioned;
  }

  public List<Account> getMentioned() {
    return mentioned;
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
