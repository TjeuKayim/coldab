package com.github.coldab.client.gui;

import com.github.coldab.shared.project.Project;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class ProjectChooserController implements Initializable {

  @FXML
  public ListView projectsListView;

  private ObservableList<Project> projects = FXCollections.observableArrayList();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    projectsListView.setItems(projects);

    projects.addAll(
        new Project("Hello World"),
        new Project("My first project!")
    );
  }
}
