package com.github.coldab.server.ws;

import com.github.coldab.server.dal.FileStore;
import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ProjectServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
  private Project project;
  private ProjectService service;
  private Account account = new Account("PietHein", "piet@hein.email");

  @Before
  public void setUp() throws Exception {
    project = new Project("MyProject");
    service = new ProjectService(project, projectStore, fileStore);
  }

  @Test
  public void connect() {
    TextFile textFile = new TextFile(1, "path/to/hello.world");
    BinaryFile binaryFile = new BinaryFile(2, "path/to/binary", "MyHash");
    project.getFiles().add(textFile);
    project.getFiles().add(binaryFile);
    // After connecting, the files should be send
    ProjectClient projectClient = Mockito.mock(ProjectClient.class);
    service.connect(projectClient, account);
    Mockito.verify(projectClient)
        .files(new TextFile[]{textFile}, new BinaryFile[]{binaryFile});
  }

  @Test
  public void newFile() {
    // When a new file is added, the change should be confirmed
    // The file should be given an id
    TextFile textFile = new TextFile(0, "path/to/hello.world");
    TextFile result = new TextFile(1, "path/to/hello.world");
    ProjectClient projectClient = Mockito.mock(ProjectClient.class);
    ProjectServer projectServer = service.connect(projectClient, account);
    projectServer.files(new TextFile[]{textFile}, null);
    Mockito.verify(projectClient, Mockito.atMost(1))
        .files(new TextFile[]{textFile}, null);
  }

  @Test
  public void subscribe() {
    // When subscribing, the client should receive all edits
    TextFile textFile = new TextFile(1, "path/to/hello.world");
    Addition addition = new Addition(0, account, null, "Hello World");
    textFile.addEdit(addition);
    project.getFiles().add(textFile);
    ProjectClient projectClient = Mockito.mock(ProjectClient.class);
    ProjectServer projectServer = service.connect(projectClient, account);
    projectServer.subscribe(1);
    Mockito.verify(projectClient)
        .edits(1, new Addition[]{addition}, null);
  }
}