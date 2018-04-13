package com.github.coldab.client.gui;

import com.github.coldab.shared.project.Project;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ColdabApplication extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/projectChooser.fxml"));
    loader.setControllerFactory(c -> new ProjectChooserController());
    Parent root = loader.load();
    stage.setTitle("Coldab text");
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  private void startEditor(Project project) throws IOException {
    Stage stage = new Stage();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editor.fxml"));
    loader.setControllerFactory(c -> new EditorController());
    Parent root = loader.load();
    stage.setTitle(String.format("%s - Coldab text", project.getName()));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
