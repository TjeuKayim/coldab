package com.github.coldab.client.gui;

import com.github.coldab.shared.project.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract class FileTree {

  static DirectoryNode createFrom(Collection<File> files) {
    // TODO: fix double nodes
    DirectoryNode root = new DirectoryNode("");
    for (File file : files) {
      String[] path = file.getPath();
      DirectoryNode parent = root;
      for (String dir : path) {
        DirectoryNode child = root.fileTrees.stream()
            .filter(f -> f instanceof DirectoryNode)
            .map(f -> (DirectoryNode) f)
            .filter(f -> f.name.equals(dir))
            .findAny()
            .orElse(new DirectoryNode(dir));
        child.fileTrees.add(new FileNode(file));
        parent.fileTrees.add(child);
        parent = child;
      }
    }
    return root;
  }

  static class DirectoryNode extends FileTree {

    private final List<FileTree> fileTrees = new ArrayList<>();

    private final String name;

    DirectoryNode(String name) {
      this.name = name;
    }

    List<FileTree> getChildren() {
      return fileTrees;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  static class FileNode extends FileTree {

    private final File file;

    FileNode(File file) {
      this.file = file;
    }

    @Override
    public String toString() {
      return file.getName();
    }
  }
}
