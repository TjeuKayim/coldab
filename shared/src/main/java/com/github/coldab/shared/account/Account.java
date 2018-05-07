package com.github.coldab.shared.account;

public class Account {
  private int id;
  private String nickName;
  private String email;

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
}
