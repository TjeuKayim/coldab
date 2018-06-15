package com.github.coldab.shared.session;

import com.github.coldab.shared.account.Account;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class VerificationToken {

  private static final int EXPIRATION = 60 * 24;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String token;

  @OneToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "account_id")
  private Account account;

  private Date expiryDate;

  public static int getExpiration() {
    return EXPIRATION;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Account getUser() {
    return account;
  }

  public void setUser(Account account) {
    this.account = account;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public VerificationToken() {
    //Spring requires an explicit empty constructor
  }
}