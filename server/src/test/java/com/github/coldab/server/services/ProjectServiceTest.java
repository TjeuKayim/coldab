package com.github.coldab.server.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.github.coldab.server.dal.AccountStore;
import com.github.coldab.server.dal.FileStore;
import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.TimeProvider;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ProjectServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectServiceTest {

  @Autowired
  private ProjectStore projectStore;
  @Autowired
  private FileStore fileStore;
  @Autowired
  private AccountStore accountStore;

  private Project project;
  private ProjectService service;
  private Account account = new Account("PietHein", "piet@hein.email");

  @Before
  public void setUp() throws Exception {
    TimeProvider.useMock();
    project = new Project("MyProject");
    service = new ProjectService(project, projectStore, fileStore, accountStore);
  }

  @Test
  public void connect() {
    TextFile textFile = new TextFile(1, "path/to/hello.world");
    BinaryFile binaryFile = new BinaryFile(2, "path/to/binary", "MyHash");
    project.getFiles().add(textFile);
    project.getFiles().add(binaryFile);
    // After connecting, the files should be send
    ProjectClient projectClient = mock(ProjectClient.class);
    service.connect(projectClient, account);
    verify(projectClient)
        .files(new TextFile[]{textFile}, new BinaryFile[]{binaryFile});
  }

  @Test
  public void newFile() {
    // When a new file is added, the change should be confirmed
    // The file should be given an id
    TextFile textFile = new TextFile(0, "path/to/hello.world");
    TextFile result = new TextFile(1, "path/to/hello.world");
    ProjectClient projectClient = mock(ProjectClient.class);
    ProjectServer projectServer = service.connect(projectClient, account);
    projectServer.files(new TextFile[]{textFile}, null);
    verify(projectClient)
        .files(new TextFile[]{textFile}, new BinaryFile[0]);
    verifyZeroInteractions(projectClient);
  }

  @Test
  public void subscribe() {
    // When subscribing, the client should receive all edits
    TextFile textFile = new TextFile(1, "path/to/hello.world");
    Addition addition = new Addition(0, account, null, "Hello World");
    textFile.addEdit(addition);
    project.getFiles().add(textFile);
    ProjectClient projectClient = mock(ProjectClient.class);
    ProjectServer projectServer = service.connect(projectClient, account);
    projectServer.subscribe(1);
    verify(projectClient)
        .edits(1, new Addition[]{addition}, new Deletion[0]);
  }

  @Test
  public void confirmAddition() {
    TextFile textFile = new TextFile(1, "path/to/hello.world");
    project.getFiles().add(textFile);
    ProjectClient client = mock(ProjectClient.class);
    ProjectServer server = service.connect(client, account);
    server.subscribe(1);
    server.addition(1, new Addition(-1, account, null, "Hello World"));
    verify(client).confirmAddition(1, new Addition(0, account, null, "Hello World"));
  }

  @Test
  public void receiveRemoteEdit() {
    TextFile textFile = new TextFile(1, "path/to/hello.world");
    project.getFiles().add(textFile);
    // Two clients that are subscribed to file 1
    ProjectClient clientA = mock(ProjectClient.class);
    ProjectServer serverA = service.connect(clientA, account);
    serverA.subscribe(1);
    ProjectClient clientB = mock(ProjectClient.class);
    ProjectServer serverB = service.connect(clientB, account);
    serverB.subscribe(1);
    // clientA sends an edit
    serverA.addition(1, new Addition(-1, account, null, "Hello World"));
    // client B should receive that
    verify(clientB).edits(1,
        new Addition[]{new Addition(0, account, null, "Hello World")},
        new Deletion[0]);
  }

  @Test
  public void unsubscribe() {
    TextFile textFile = new TextFile(1, "path/to/hello.world");
    project.getFiles().add(textFile);
    // Two clients that are subscribed to file 1
    ProjectClient clientA = mock(ProjectClient.class);
    ProjectServer serverA = service.connect(clientA, account);
    serverA.subscribe(1);
    ProjectClient clientB = mock(ProjectClient.class);
    ProjectServer serverB = service.connect(clientB, account);
    serverB.subscribe(1);
    serverB.unsubscribe(1);
    // clientA sends an edit, but client B shouldn't receive that
    serverA.addition(1, new Addition(-1, account, null, "Hello World"));
    verify(clientB, never()).edits(anyInt(), any(), any());
  }

  @Test
  public void editCounter() {
    TextFile textFile = new TextFile(1, "path/to/hello.world");
    project.getFiles().add(textFile);
    ProjectClient client = mock(ProjectClient.class);
    ProjectServer server = service.connect(client, account);
    server.subscribe(1);
    server.addition(1, new Addition(-1, account, null, "Hello World"));
    verify(client).confirmAddition(1, new Addition(0, account, null, "Hello World"));
    server.addition(1, new Addition(-2, account, null, "Hello World"));
    verify(client).confirmAddition(1, new Addition(1, account, null, "Hello World"));
  }
}