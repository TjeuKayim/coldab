package com.github.coldab.server.dal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.github.coldab.server.Main;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test for database.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@ActiveProfiles(profiles = "dev")
public class ProjectStoreTest {

  @Autowired
  private ProjectStore projectStore;
  @Autowired
  private AccountStore accountStore;
  @Autowired
  private FileStore fileStore;

  @Test
  public void getById() {
    Project project = new Project("Hello World");

    int id = projectStore.save(project).getId();

    Project result = projectStore.findById(id)
        .orElseThrow(IllegalStateException::new);

    assertEquals("Hello World", result.getName());
  }

  @Test
  public void saveTextFile() {
    // Create account
    Account account = createAccount("saveTextFile");
    accountStore.save(account);
    // Create textFile with edit
    TextFile textFile = new TextFile(0, "index.html");
    textFile.addEdit(new Addition(0, account, null, "Hello World"));
    fileStore.save(textFile);
    // Create project, and add textFle
    Project project = new Project("Hello World");
    project.getFiles().add(textFile);
    int id = projectStore.save(project).getId();
    // After saving everything, get it back and assert if equal
    project = projectStore.findById(id).orElseThrow(NullPointerException::new);
    assertEquals(1, project.getFiles().size());
    TextFile actual = (TextFile) project.getFiles().iterator().next();
    assertEquals(textFile.getId(), actual.getId());
    assertArrayEquals(textFile.getPath(), actual.getPath());
    assertEquals(textFile.getEdits().get(0), actual.getEdits().get(0));
  }

  @Test
  public void reproduce_issue_20() {
    // Bob creates a project
    Account bob = createAccount("Bob");
    Project project = projectStore.save(new Project("MyProject"));
    project.getAdmins().add(bob);
    // Add a file
    TextFile file = new TextFile(0, "hello.world");
    fileStore.save(file);
    project.getFiles().add(file);
    projectStore.save(project);
    // Share with alice
    Account alice = createAccount("Alice");
    project.getAdmins().add(alice);
    projectStore.save(project);
  }

  private Account createAccount(String name) {
    return accountStore.save(new Account(name, name, name));
  }
}