package com.github.coldab.client.project;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ProjectServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ProjectComponentTest {

  private ProjectServer projectServer;
  private Project project;
  private Account account;
  private ProjectObserver projectObserver;

  @Before
  public void setUp() throws Exception {
    projectServer = Mockito.mock(ProjectServer.class);
    project = new Project();
    account = new Account("Test", "Test@email.com", "test");
    projectObserver = Mockito.mock(ProjectObserver.class);
  }

  @Test
  public void collaborators() {

  }

  @Test
  public void files() {
  }

  @Test
  public void removeFile() {
  }

  @Test
  public void edits() {
  }

  @Test
  public void confirmAddition() {
  }

  @Test
  public void confirmDeletion() {
  }

  @Test
  public void caret() {
  }

  @Test
  public void openFile() {
  }

  @Test
  public void closeFile() {
  }

  @Test
  public void createFile() {
  }

  @Test
  public void deleteFile() {
  }
}