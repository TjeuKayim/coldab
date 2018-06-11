package com.github.coldab.client.gui;

import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.rest.AccountServer;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

  private final Consumer<Project> resultCallback;
  @FXML
  public ListView<Project> projectsListView;

  private final ObservableList<Project> projects = FXCollections.observableArrayList();

  private final AccountServer accountServer;

  public ProjectChooserController(AccountServer accountServer,
      Consumer<Project> resultCallback) {
    this.accountServer = accountServer;
    this.resultCallback = resultCallback;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    projectsListView.setItems(projects);
    projectsListView.setCellFactory(ProjectRow::new);

    List<Project> updatedProjects = accountServer.getProjects();
    if (updatedProjects != null) {
      this.projects.addAll(updatedProjects);
    }
  }

  private void openProject(Project project) {
    resultCallback.accept(project);
  }
  @FXML
  private void addNewProject(ActionEvent actionEvent){
    
  }
  @FXML
  public void refreshProjects(ActionEvent actionEvent){
    projectsListView.setItems(projects);
    projectsListView.setCellFactory(ProjectRow::new);

    projects.addAll(accountServer.getProjects());
  }



  private class ProjectRow extends ListCell<Project> {

    private final HBox box;
    private final Label label;
    private final Button open;

    ProjectRow(ListView<Project> listView) {
      label = new Label();
      Pane pane = new Pane();
      HBox.setHgrow(pane, Priority.ALWAYS);
      open = new Button("Open");
      Button remove = new Button(null, new FontIcon(FontAwesomeRegular.TRASH_ALT));
      box = new HBox(label, pane, open, remove);
    }

    @Override
    protected void updateItem(Project project, boolean empty) {
      super.updateItem(project, empty);
      if (project != null) {
        setText(null);
        label.setText(project.getName());
        open.setOnAction(event -> openProject(project));
        setGraphic(box);
      }
    }

  }
}
