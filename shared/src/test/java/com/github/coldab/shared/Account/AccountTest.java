package com.github.coldab.shared.Account;

import com.github.coldab.shared.account.Account;
import org.junit.Before;
import org.junit.Test;

public class AccountTest {

  private Account account;

  @Before
  public void setup(){
    account = new Account("TestUser","Test@user.com");
  }

  @Test
  public void validate(){

  }
}

