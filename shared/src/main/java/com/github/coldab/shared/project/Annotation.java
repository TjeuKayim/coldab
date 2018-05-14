package com.github.coldab.shared.project;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.edit.Letter;
import java.time.LocalDateTime;

public class Annotation extends Edit {
  private final boolean todo;
  private final String text;

  public Annotation(Account account, LocalDateTime creationDate,
      Letter start, boolean todo, String text) {
    super(account, creationDate, start);
    this.todo = todo;
    this.text = text;
  }
}
