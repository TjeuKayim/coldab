package com.github.coldab.client.project;

import static org.junit.Assert.assertEquals;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Position;
import com.github.coldab.shared.project.TextFile;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;


public class TextFileComponentTest {

  private TextFile file;
  private Account account;
  private TextFileController controller;
  private TextFileClient client;
  private TextFileObserverMock observerMock;

  @Before
  public void setUp() throws Exception {
    file = new TextFile("/test.txt");
    account = new Account("PietHein", "piet@hein.email");
    TextFileComponent component = new TextFileComponent(file, account, null);
    controller = component;
    client = component;
    observerMock = new TextFileObserverMock();
    controller.addObserver(observerMock);
  }

  @Test
  public void remoteEdits() {
    Addition alpha = new Addition(0, account, null, "Hello");
    Addition beta = new Addition(1, account, new Position(0, 4), " World");
    client.newEdit(alpha);
    client.newEdit(beta);
    assertEquals(file.getEdits(), Arrays.asList(alpha, beta));
  }

  @Test
  public void remoteAnnotation() {
  }
}