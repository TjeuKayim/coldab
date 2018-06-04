package com.github.coldab.client.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthenticationController implements Initializable {

  @FXML
  private TextField nickname;
  @FXML
  private TextField password;
  @FXML
  private Hyperlink register;
  @FXML
  private Button login;

  private Stage registerStage;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    register.setOnAction(this::registerHyperlinkClicked);
  }

  private void registerHyperlinkClicked(ActionEvent actionEvent) {
    //System.out.println(actionEvent.toString());

    Stage stage = new Stage();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
    loader.setControllerFactory(c -> new RegisterController());
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException e) {
      throw new IllegalStateException("Could not load FXML");
    }
    stage.setTitle(String.format("Register Coldab Account"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

}
