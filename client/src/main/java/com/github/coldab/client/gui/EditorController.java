package com.github.coldab.client.gui;

import com.github.coldab.client.gui.FileTree.DirectoryNode;
import com.github.coldab.shared.project.File;
import com.github.coldab.shared.project.TextFile;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

public class EditorController implements Initializable {
  public TreeView<String> fileTreeView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    updateFileTree();
  }

  private void updateFileTree() {
    // Create root
    TreeItem<String> rootItem = new TreeItem<>();

    // Test files
    LocalDateTime now = LocalDateTime.now();
    Collection<File> files = Arrays.asList(
        new TextFile("path/to/file.txt", now),
        new TextFile("path/to/another-file.txt", now),
        new TextFile("website/index.html", now)
    );

    DirectoryNode fileTree = FileTree.createFrom(files);

    // Add files
    addNodesToFileTree(rootItem, fileTree);

    fileTreeView.setShowRoot(false);
    fileTreeView.setRoot(rootItem);
  }

  private void addNodesToFileTree(TreeItem<String> treeItem, DirectoryNode fileTree) {
    for (FileTree child : fileTree.getChildren()) {
      TreeItem<String> childItem = new TreeItem<>(child.toString());
      treeItem.getChildren().add(childItem);
      if (child instanceof DirectoryNode) {
        // Directory
        childItem.expandedProperty().addListener((observable, oldValue, newValue) -> {
          FontAwesomeRegular iconCode =
              newValue ? FontAwesomeRegular.FOLDER_OPEN : FontAwesomeRegular.FOLDER;
          childItem.setGraphic(new FontIcon(iconCode));
        });
        childItem.setExpanded(true);
        addNodesToFileTree(childItem, (DirectoryNode) child);
      } else {
        // File
        childItem.setGraphic(new FontIcon(FontAwesomeRegular.FILE_ALT));
      }
    }
  }
}
