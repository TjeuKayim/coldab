package com.github.coldab.shared.project;

import com.github.coldab.shared.TimeProvider;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Position;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

// TODO: Should annotation have a list of mentioned accounts?
@Entity
public class Annotation {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @Column(nullable = false)
  private boolean todo;

  @Column(nullable = false)
  private String text;

  @ManyToOne(cascade = CascadeType.ALL)
  private Account account;

  @Column(nullable = false)
  private LocalDateTime creationDate;

  private Position start;

  public Annotation(Account account,
      Position start, boolean todo, String text) {
    this.account = account;
    this.creationDate = TimeProvider.getInstance().now();
    this.start = start;
    this.todo = todo;
    this.text = text;
  }

  // this constructor is used by JPA.
  protected Annotation() {
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

  public Position getStart() {
    return start;
  }
}