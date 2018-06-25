package com.github.coldab.client.gui;

import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.rest.AccountServer;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.TextInputDialog;
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
    refreshProjects();
  }

  /**
   * create a new project, where you are the admin.
   */
  @FXML
  private void addNewProject(ActionEvent actionEvent) {
    TextInputDialog dialog = new TextInputDialog("");
    dialog.setTitle("New Project");
    dialog.setHeaderText("New Project");
    dialog.setContentText("ProjectName");
    Optional<String> result = dialog.showAndWait();
    result.ifPresent(projectName -> {
      if (projectName.isEmpty()) {
        return;
      }
      accountServer.createProject(result.get());
      refreshProjects();
    });
  }

  /**
   * refresh the list of projects in the gui.
   */
  @FXML
  public void refreshProjects() {
    projectsListView.getItems().clear();
    projectsListView.setItems(projects);
    projectsListView.setCellFactory(ProjectRow::new);

    List<Project> updatedProjects = accountServer.getProjects();
    if (updatedProjects != null) {
      this.projects.addAll(updatedProjects);
    }
  }


  private class ProjectRow extends ListCell<Project> {

    private final HBox box;
    private final Label label;
    private final Button open;
    private final Button remove;

    ProjectRow(ListView<Project> listView) {
      label = new Label();
      Pane pane = new Pane();
      HBox.setHgrow(pane, Priority.ALWAYS);
      open = new Button("Open");
      remove = new Button(null, new FontIcon(FontAwesomeRegular.TRASH_ALT));
      box = new HBox(label, pane, open, remove);
    }

    @Override
    protected void updateItem(Project project, boolean empty) {
      super.updateItem(project, empty);
      if (project != null) {
        setText(null);
        label.setText(project.getName());
        open.setOnAction(event -> openProject(project));
        remove.setOnAction(event -> removeProject(project));
        setGraphic(box);
      }
    }

    /**
     * open the editor with the selected project.
     */
    private void openProject(Project project) {
      resultCallback.accept(project);
    }

    private void removeProject(Project project) {
      if (accountServer.removeProject(project)) {
        projectsListView.getItems().remove(project);
      }
    }
  }
}
