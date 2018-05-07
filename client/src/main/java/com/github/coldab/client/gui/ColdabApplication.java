package com.github.coldab.client.gui;

import com.github.coldab.shared.project.Project;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ColdabApplication extends Application {

  private Stage projectChooserStage;

  @Override
  public void start(Stage primaryStage) throws IOException {
    this.projectChooserStage = primaryStage;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/projectChooser.fxml"));
    loader.setControllerFactory(c -> new ProjectChooserController(this::startEditor));
    Parent root = loader.load();
    projectChooserStage.setTitle("Coldab text");
    Scene scene = new Scene(root);
    projectChooserStage.setScene(scene);
    projectChooserStage.show();
  }

  private void startEditor(Project project) {
    projectChooserStage.hide();
    Stage stage = new Stage();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editor.fxml"));
    loader.setControllerFactory(c -> new EditorController(project));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException e) {
      throw new IllegalStateException("Could not load FXML");
    }
    stage.setTitle(String.format("%s - Coldab text", project.getName()));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    stage.setOnCloseRequest(event -> projectChooserStage.show());
  }

  public static void main(String[] args) {
    launch(args);
  }
}
