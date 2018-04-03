package com.github.coldab.client.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.springframework.beans.factory.annotation.Autowired;

public class MainController implements Initializable {
  public TreeView<String> fileTree;

  @Autowired
  private Greeter greeter;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    TreeItem<String> rootItem = new TreeItem<String>(greeter.sayHello());
    rootItem.setExpanded(true);
    for (int i = 1; i < 6; i++) {
      TreeItem<String> item = new TreeItem<String>("File " + i);
      rootItem.getChildren().add(item);
    }
    fileTree.setRoot(rootItem);
  }
}
