package com.github.coldab.client.gui;

import static org.junit.Assert.assertEquals;

import com.github.coldab.client.gui.FileTree.DirectoryNode;
import com.github.coldab.shared.project.File;
import com.github.coldab.shared.project.TextFile;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Test;

public class FileTreeTest {

  @Test
  public void createFrom() {
    // Test files
    LocalDateTime now = LocalDateTime.now();
    Collection<File> files = Arrays.asList(
        new TextFile("path/to/file.txt", now),
        new TextFile("path/to/another-file.txt", now),
        new TextFile("website/index.html", now)
    );

    DirectoryNode fileTree = FileTree.createFrom(files);

    List<FileTree> children = fileTree.getChildren();

    // Directories
    assertEquals("first dir should be 'path'", "path", children.get(0).toString());
    assertEquals("second dir should be 'website'", "website", children.get(1).toString());
    assertEquals("size should be 2", 2, children.size());

    // Subdirectory path
    DirectoryNode path = (DirectoryNode) children.get(0);
    List<FileTree> pathChildren = path.getChildren();
    assertEquals(1, pathChildren.size());
    DirectoryNode to = (DirectoryNode) pathChildren.get(0);
    assertEquals("to", to.toString());
    assertEquals(2, to.getChildren().size());
    assertEquals("file.txt", to.getChildren().get(0).toString());
    assertEquals("another-file.txt", to.getChildren().get(1).toString());

    // Subdirectory website
    DirectoryNode website = (DirectoryNode) children.get(1);
    assertEquals(1, website.getChildren().size());
    assertEquals("index.html", website.getChildren().get(0).toString());
  }
}