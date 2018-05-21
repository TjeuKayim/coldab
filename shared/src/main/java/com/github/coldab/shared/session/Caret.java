package com.github.coldab.shared.session;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Position;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Caret caret = (Caret) o;
    return Objects.equals(account, caret.account) &&
        Objects.equals(position, caret.position);
  }

  @Override
  public int hashCode() {

    return Objects.hash(account, position);
  }
}
