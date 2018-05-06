package com.github.coldab.client.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class AuthenticationController implements Initializable {

  @FXML
  private WebView googleWebview;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    WebEngine browser = googleWebview.getEngine();

    googleWebview.prefHeight(400);
    googleWebview.prefWidth(600);

    browser.load("https://google.nl");
  }
}
