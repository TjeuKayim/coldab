package com.github.coldab.client.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthenticationController implements Initializable {

  @FXML
  private WebView googleWebview;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    String url = getClass().getResource("/html/index.html").toExternalForm();

    WebEngine browser = googleWebview.getEngine();

    googleWebview.prefHeight(400);
    googleWebview.prefWidth(600);

    browser.load(url);


  }
}
