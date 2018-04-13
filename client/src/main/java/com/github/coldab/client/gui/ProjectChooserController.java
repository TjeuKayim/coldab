package com.github.coldab.client.gui;

import com.github.coldab.shared.project.Project;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

public class ProjectChooserController implements Initializable {

  @FXML
  public ListView<Project> projectsListView;

  private ObservableList<Project> projects = FXCollections.observableArrayList();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    projectsListView.setItems(projects);
    projectsListView.setCellFactory(ProjectRow::new);

    projects.addAll(
        new Project("Hello World"),
        new Project("My first project!")
    );
  }

  private void openProject(Project project) {

  }

  private class ProjectRow extends ListCell<Project> {

    private final HBox hBox;
    private final Label label;
    private final Button open;

    ProjectRow(ListView<Project> listView) {
      label = new Label();
      Pane pane = new Pane();
      HBox.setHgrow(pane, Priority.ALWAYS);
      open = new Button("Open");
      Button remove = new Button(null, new FontIcon(FontAwesomeRegular.TRASH_ALT));
      hBox = new HBox(label, pane, open, remove);
    }

    @Override
    protected void updateItem(Project project, boolean empty) {
      super.updateItem(project, empty);
      if (project != null) {
        setText(null);
        label.setText(project.getName());
        open.setOnAction(event -> openProject(project));
        setGraphic(hBox);
      }
    }
  }
}
