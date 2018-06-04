package com.github.coldab.shared.account;

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
}
