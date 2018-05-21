package com.github.coldab.shared.session;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Position;

public class Caret {

  private final Account account;
  private final Position position;

  public Caret(Account account, Position position) {
    this.account = account;
    this.position = position;
  }

  public Account getAccount() {
    return account;
  }

  public Position getPosition() {
    return position;
  }
}
