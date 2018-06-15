package com.github.coldab.shared.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ProjectTest {

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  private Project project;
  private File file1;
  private File file2;

  @Before
  public void setup() {
    this.project = new Project("TestProject");
    this.file1 = new TextFile(1, "/home/test/123");
    this.file2 = new TextFile(2, "/home/test/124");
    project.getFiles().add(file1);


  }

  @Test
  public void getfilebyid() {
    assertSame("getfilebyid returns incorrect file", project.getFileById(1), file1);

    exception.expect(IllegalArgumentException.class);
    project.getFileById(2);

  }

  @Test
  public void updatefile() {
    file1.setPath(file2.getPath());
    project.updateFile(file1);
    assertEquals("failed update", project.getFileById(1).getPath(), file2.getPath());
  }

}
