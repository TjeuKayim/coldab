package com.github.coldab.client.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AuthenticationController implements Initializable {

  @FXML
  private TextField username;
  @FXML
  private TextField password;
  @FXML
  private Button login;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }
}
