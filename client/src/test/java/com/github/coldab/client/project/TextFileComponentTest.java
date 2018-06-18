package com.github.coldab.client.project;

import static org.junit.Assert.assertEquals;

import com.github.coldab.shared.TimeProvider;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Position;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.ws.TextFileClient;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;


public class TextFileComponentTest {

  private TextFile file;
  private Account account;
  private TextFileController controller;
  private TextFileClient client;
  private TextFileObserverMock observerMock;
  private TextFileServerMock serverMock;

  @Before
  public void setUp() {
    TimeProvider.useMock();
    file = new TextFile(0, "/test.txt");
    account = new Account("PietHein", "piet@hein.email", "1234");
    serverMock = new TextFileServerMock();
    TextFileComponent component = new TextFileComponent(file, account, serverMock);
    controller = component;
    client = component;
    observerMock = new TextFileObserverMock();
    controller.addObserver(observerMock);
  }

  @Test
  public void remoteEdits() {
    Addition alpha = new Addition(0, account, null, "Hello");
    assertEquals("", observerMock.getText());
    client.newEdit(alpha);
    assertEquals("Hello", observerMock.getText());
    // Add another edit
    Position betaPosition = new Position(0, 4);
    Addition beta = new Addition(1, account, betaPosition, " World");
    client.newEdit(beta);
    assertEquals("Hello World", observerMock.getText());
    assertEquals(file.getEdits(), Arrays.asList(alpha, beta));
  }

  @Test
  public void localAdditions() {
    controller.createAddition(-1, "Hello");
    Addition alpha = new Addition(-1, account, null, "Hello");
    assertEquals(alpha, serverMock.getEdit());
    // Second addition
    controller.createAddition(4, " World");
    Position betaPosition = new Position(alpha.getIndex(), 4);
    Addition beta = new Addition(-2, account, betaPosition, " World");
    assertEquals(beta, serverMock.getEdit());
  }
}