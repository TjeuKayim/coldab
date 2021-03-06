package com.github.coldab.shared.account;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AccountTest {

  private Account account;
  private String correct;
  private String incorrect;

  @Before
  public void setup() {
    account = new Account("TestUser", "Test@user.com", "Strongpassword");
    correct = "Strongpassword";
    incorrect = "weakpassword";
  }

  @Test
  public void validate() {
    assertTrue("false negative on validate password", account.validatePassword(correct));
    assertFalse("false positive on validate password", account.validatePassword(incorrect));
  }
}

