package com.github.coldab.shared.account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String nickName;
  private String email;

  // this constructor is used by JPA.
  protected Account() {
  }

  public Account(String nickName, String email) {
    this.nickName = nickName;
    this.email = email;
  }
}
