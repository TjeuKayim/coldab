package com.github.coldab.client.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.stereotype.Component;

@Component
public class MainController implements Initializable {
  public TreeView<String> fileTree;

  private final Greeter greeter;

  public MainController(Greeter greeter) {
    this.greeter = greeter;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    TreeItem<String> rootItem = new TreeItem<>(greeter.sayHello());
    rootItem.expandedProperty().addListener((observable, oldValue, newValue) -> {
      FontAwesomeRegular iconCode =
          newValue ? FontAwesomeRegular.FOLDER_OPEN : FontAwesomeRegular.FOLDER;
      rootItem.setGraphic(new FontIcon(iconCode));
    });
    rootItem.setExpanded(true);
    for (int i = 1; i < 6; i++) {
      FontIcon fileIcon = new FontIcon(FontAwesomeRegular.FILE_ALT);
      TreeItem<String> item = new TreeItem<>("File " + i, fileIcon);
      rootItem.getChildren().add(item);
    }
    fileTree.setRoot(rootItem);
  }
}
