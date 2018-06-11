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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test for database.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
public class ProjectStoreTest {

  @Autowired
  private ProjectStore projects;

  @Test
  public void getById() {
    Project project = new Project("Hello World");

    int id = projects.save(project).getId();

    Project result = projects.findById(id)
        .orElseThrow(IllegalStateException::new);

    assertEquals("Hello World", result.getName());
  }

  @Test
  public void saveTextFile() {
    Project project = new Project("Hello World");
    TextFile textFile = new TextFile(0, "index.html");
    Account piet = new Account("Piet Hein", "piet@hein.email");
    textFile.addEdit(new Addition(0, piet, null, "Hello World"));
    project.getFiles().add(textFile);
    int id = projects.save(project).getId();
    project = projects.findById(id).orElseThrow(NullPointerException::new);
    assertEquals(1, project.getFiles().size());
    TextFile actual = (TextFile) project.getFiles().get(0);
    assertEquals(textFile.getId(), actual.getId());
    assertArrayEquals(textFile.getPath(), actual.getPath());
    assertEquals(textFile.getEdits().get(0), actual.getEdits().get(0));
  }
}