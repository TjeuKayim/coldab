package com.github.coldab.shared.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.coldab.shared.project.Project;
import com.google.gson.annotations.Expose;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Account {

  @Transient
  @Expose
  private String sessionId = null;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  @Expose
  private int id;

  @Column(unique = true)
  @Expose
  private String nickName;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  @Column(unique = true, nullable = false)
  @Expose
  private String email;

  /**
   * This constructor is used by JPA.
   */
  protected Account() {
  }

  public Account(String nickName, String email, String password) {
    this.nickName = nickName;
    this.email = email;
    this.password = password;
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

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  /**
   * validate if a given password matches the password of the account.
   *
   * @param password the input password
   * @return Boolean true if the passwords match, else returns false.
   */
  public boolean validatePassword(String password) {
    return this.password.equals(password);
  }

  /**
   * Checks if this account is either collaborator or admin in the project.
   */
  public boolean isMemberOf(Project project) {
    return project.getAdmins().contains(this) || project.getCollaborators().contains(this);
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
