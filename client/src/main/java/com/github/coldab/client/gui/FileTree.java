package com.github.coldab.client.gui;

import com.github.coldab.shared.project.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

interface FileTree {

  /**
   * Creates a file-tree from a list of files.
   * @param files files with a path
   * @return a tree with files stored in directories
   */
  static DirectoryNode createFrom(Collection<File> files) {
    DirectoryNode root = new DirectoryNode("");
    for (File file : files) {
      String[] path = file.getPath();
      DirectoryNode parent = root;
      for (int i = 0; i < path.length - 1; i++) {
        String dir = path[i];
        Optional<DirectoryNode> child = parent.children.stream()
            .map(f -> (DirectoryNode) f)
            .filter(f -> f.name.equals(dir))
            .findAny();
        if (!child.isPresent()) {
          // Create new directory
          DirectoryNode newDir = new DirectoryNode(dir);
          parent.children.add(newDir);
          child = Optional.of(newDir);
        }
        parent = child.get();
      }
      parent.children.add(new FileNode(file));
    }
    return root;
  }

  class DirectoryNode implements FileTree {

    private final List<FileTree> children = new ArrayList<>();

    private final String name;

    DirectoryNode(String name) {
      this.name = name;
    }

    List<FileTree> getChildren() {
      return children;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  class FileNode implements FileTree {

    private final File file;

    FileNode(File file) {
      this.file = file;
    }

    public File getFile() {
      return file;
    }

    @Override
    public String toString() {
      return file.getName();
    }
  }
}
