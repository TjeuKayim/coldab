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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthenticationController implements Initializable {

  @FXML
  private TextField email;
  @FXML
  private TextField password;
  @FXML
  private Hyperlink register;
  @FXML
  private Button login;
  @FXML
  private Label emailWarning;
  @FXML
  private Label passwordWarning;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    register.setOnAction(this::registerHyperlinkClicked);
    login.setOnAction(this:: loginButtonClicked);
  }

  private void loginButtonClicked(ActionEvent actionEvent) {
    emailWarning.setText("");
    passwordWarning.setText("");

    if(email.getText().trim().isEmpty() || email.getText() == null){
      emailWarning.setText("Username Required!");
    }
    if(password.getText().trim().isEmpty() || password.getText() == null){
      passwordWarning.setText("Password Required!");
    }
  }

  private void registerHyperlinkClicked(ActionEvent actionEvent) {
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
