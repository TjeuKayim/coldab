package com.github.coldab.server.services;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.github.coldab.server.dal.AccountStore;
import com.github.coldab.server.dal.FileStore;
import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ProjectServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test for sharing a project.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ShareTest {

  @Autowired
  private ProjectStore projectStore;
  @Autowired
  private FileStore fileStore;
  @Autowired
  private AccountStore accountStore;

  @Test
  public void share_project_and_create_file() {
    // Create two accounts
    Account bob = accountStore.save(new Account("bob", "bob", "bob"));
    Account alice = accountStore.save(new Account("alice", "alice", "alice"));
    // Create a project and add Bob as admin
    Project project = new Project("ProjectToShare");
    project.getAdmins().add(bob);
    project = projectStore.save(project);
    ProjectService projectService =
        new ProjectService(project, projectStore, fileStore, accountStore);
    // Connect Bob
    Connection bobCon = new Connection(bob, projectService);
    // Bob shares the project with alice
    bobCon.server.share("alice", true);
    // Alice connects
    Connection aliceCon = new Connection(alice, projectService);
    // Bob creates a file
    bobCon.server.files(new TextFile[]{new TextFile(0, "myFile")}, null);
    // Verify that alice receives it
    verify(aliceCon.mock)
        .files(argThat(x -> x[0].getName().equals("myFile")), eq(new BinaryFile[0]));
  }

  @Test
  public void create_file_and_share_project() {
    // Create two accounts
    Account bob = accountStore.save(new Account("bob", "bob", "bob"));
    Account alice = accountStore.save(new Account("alice", "alice", "alice"));
    // Create a project and add Bob as admin
    Project project = new Project("ProjectToShare");
    project.getAdmins().add(bob);
    project = projectStore.save(project);
    ProjectService projectService =
        new ProjectService(project, projectStore, fileStore, accountStore);
    // Connect Bob
    Connection bobCon = new Connection(bob, projectService);
    // Bob creates a file
    bobCon.server.files(new TextFile[]{new TextFile(0, "myFile")}, null);
    // Bob shares the project with alice
    bobCon.server.share("alice", true);
    // Alice connects
    Connection aliceCon = new Connection(alice, projectService);
    // Verify that alice receives it
    verify(aliceCon.mock)
        .files(argThat(x -> x[0].getName().equals("myFile")), eq(new BinaryFile[0]));
  }

  private class Connection {

    final ProjectClient mock;
    final ProjectServer server;

    Connection(Account account, ProjectService projectService) {
      mock = mock(ProjectClient.class);
      server = projectService.connect(mock, account);
    }
  }
}
