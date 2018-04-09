package com.github.coldab.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ColdabApplication extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
    loader.setControllerFactory(context::getBean);
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
