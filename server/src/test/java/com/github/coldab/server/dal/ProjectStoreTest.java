package com.github.coldab.server.dal;

import static org.junit.Assert.assertEquals;

import com.github.coldab.shared.project.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@DataJpaTest
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
}