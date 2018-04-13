package com.github.coldab.client.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ColdabApplication extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
  }

  private void startEditor() throws IOException {
    Stage primaryStage = new Stage();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editor.fxml"));
    loader.setControllerFactory(c -> new EditorController());
    Parent root = loader.load();
    primaryStage.setTitle("Coldab text");
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
