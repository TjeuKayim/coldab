package com.github.coldab.shared.account;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @Column(unique=true,  nullable = false)
  private String nickName;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String email;

  /**
   * This constructor is used by JPA.
   */
  protected Account() {
  }

  public Account(String nickName, String email) {
    this.nickName = nickName;
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public String getNickName() {
    return nickName;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Account)) {
      return false;
    }
    Account account = (Account) o;
    return id == account.id &&
        Objects.equals(nickName, account.nickName) &&
        Objects.equals(email, account.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nickName, email);
  }
}
