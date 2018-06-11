package com.github.coldab.client.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AuthenticationController implements Initializable {

  @FXML
  private TextField email;
  @FXML
  private TextField password;
  @FXML
  private Button login;
  @FXML
  private Button register;
  @FXML
  private Label emailWarning;
  @FXML
  private Label passwordWarning;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    register.setOnAction(this::registerButtonClicked);
    login.setOnAction(this:: loginButtonClicked);
  }

  private void registerButtonClicked(ActionEvent actionEvent) {
    if(checkInput()){
      //TODO: register the user
    }
  }

  private void loginButtonClicked(ActionEvent actionEvent) {
    if(checkInput()){
      //TODO: Login the user
    }
  }

  /**
   * Check if form input are not empty
   * @return boolean
   */
  private boolean checkInput() {
    boolean input = true;

    emailWarning.setText("");
    passwordWarning.setText("");

    if(email.getText().trim().isEmpty() || email.getText() == null){
      emailWarning.setText("Username Required!");

      input = false;
    }
    if(password.getText().trim().isEmpty() || password.getText() == null){
      passwordWarning.setText("Password Required!");

      input = false;
    }

    return input;
  }
}
