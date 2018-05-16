package com.github.coldab.client.project;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.TextFile;
import org.junit.Before;

public class TextFileComponentTest {

  private TextFile file;
  private Account account;
  private TextFileComponent component;
  private TextFileObserverMock observerMock;

  @Before
  public void setUp() throws Exception {
    file = new TextFile("/test.txt");
    account = new Account("PietHein", "piet@hein.email");
    component = new TextFileComponent(file, account, null);
    observerMock = new TextFileObserverMock();
    component.addObserver(observerMock);
  }


}