package com.github.coldab.client.gui;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.rest.AccountServer;
import com.github.coldab.shared.rest.Credentials;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

  private final AccountServer accountServer;

  private Consumer<Account> callback;

  public AuthenticationController(AccountServer accountServer, Consumer<Account> callback) {
    this.accountServer = accountServer;
    this.callback = callback;
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    register.setOnAction(this::registerButtonClicked);
    login.setOnAction(this::loginButtonClicked);
  }

  /**
   * try to login using the credentials filled in on the gui.
   * if the login is unsuccessful show an alert.
   */
  private void loginButtonClicked(ActionEvent actionEvent) {
    if (checkInput()) {
      Account account = accountServer.login(new Credentials(email.getText(), password.getText()));

      if (account != null) {
        callback.accept(account);
      } else {
        showAlert("Login Error", "Unable to login!");
      }
    }
  }

  /**
   * register a new account  with te credentials from the gui.
   * if the registration fails , show an alert.
   * @param actionEvent
   */
  private void registerButtonClicked(ActionEvent actionEvent) {
    if (checkInput()) {
      Account account = accountServer
          .register(new Credentials(email.getText(), password.getText()));

      if (account != null) {
        callback.accept(account);
      } else {
        showAlert("Register Error", "Unable to register account!");
      }
    }
  }

  /**
   * Show alert when unable to login or register
   */
  private void showAlert(String alertTitle, String alertMessage) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle(alertTitle);
    alert.setHeaderText(null);
    alert.setContentText(alertMessage);
    alert.showAndWait();
  }

  /**
   * Check if form input are not empty
   *
   * @return boolean
   */
  private boolean checkInput() {
    boolean input = true;

    emailWarning.setText("");
    passwordWarning.setText("");

    if (email.getText().trim().isEmpty() || email.getText() == null) {
      emailWarning.setText("Username Required!");

      input = false;
    }
    if (password.getText().trim().isEmpty() || password.getText() == null) {
      passwordWarning.setText("Password Required!");

      input = false;
    }

    return input;
  }
}
