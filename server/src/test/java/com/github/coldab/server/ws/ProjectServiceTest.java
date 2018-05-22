package com.github.coldab.server.ws;

import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ProjectServer;
import java.util.Collections;
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
  private Project project;
  private ProjectService service;
  private Account account = new Account("PietHein", "piet@hein.email");

  @Before
  public void setUp() throws Exception {
    project = new Project("MyProject");
    service = new ProjectService(project, projectStore);
  }

  @Test
  public void connect() {
    TextFile textFile = new TextFile(1, "path/to/hello.world");
    BinaryFile binaryFile = new BinaryFile(2, "path/to/binary", "MyHash");
    project.getFilesById().put(1, textFile);
    project.getFilesById().put(2, binaryFile);
    // After connecting, the files should be send
    ProjectClient projectClient = Mockito.mock(ProjectClient.class);
    ProjectServer projectServer = service.connect(projectClient, account);
    Mockito.verify(projectClient)
        .files(Collections.singletonList(textFile), Collections.singletonList(binaryFile));
  }
}