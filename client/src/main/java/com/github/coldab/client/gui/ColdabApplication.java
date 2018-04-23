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
  private Stage authenticationStage;

  @Override
  public void start(Stage primaryStage) throws IOException {
      this.authenticationStage = primaryStage;
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/authentication.fxml"));
      loader.setControllerFactory(c -> new AuthenticationController());
      Parent root = loader.load();
      authenticationStage.setTitle("Coldab text");
      Scene scene = new Scene(root);
      authenticationStage.setScene(scene);
      authenticationStage.show();


//    this.projectChooserStage = primaryStage;
//    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/projectChooser.fxml"));
//    loader.setControllerFactory(c -> new ProjectChooserController(this::startEditor));
//    Parent root = loader.load();
//    projectChooserStage.setTitle("Coldab text");
//    Scene scene = new Scene(root);
//    projectChooserStage.setScene(scene);
//    projectChooserStage.show();
  }

  private void startEditor(Project project) {
    projectChooserStage.hide();
    Stage stage = new Stage();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editor.fxml"));
    loader.setControllerFactory(c -> new EditorController());
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

  private void startAuthentication(Project project){
    projectChooserStage.hide();
    Stage stage = new Stage();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/authentication.fxml"));
    loader.setControllerFactory(c -> new AuthenticationController());
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException e) {
      throw new IllegalStateException("Could not load FXML");
    }
    stage.setTitle(String.format("%s - Coldab text"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
