package com.github.coldab.client.gui;

import com.github.coldab.client.project.ChatComponent;
import com.github.coldab.client.project.ProjectComponent;
import com.github.coldab.client.rest.RestClient;
import com.github.coldab.client.ws.WebSocketConnection;
import com.github.coldab.client.ws.WebSocketEndpoint;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ColdabApplication extends Application {

  private Stage projectChooserStage;
  private Stage authenticationStage;
  private RestClient restClient = new RestClient();

  private static final Logger LOGGER = Logger.getLogger(ColdabApplication.class.getName());
  private Stage editorStage;

  /**
   * open the login/register screen, if te login/regiser is successful, open the project chooser.
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    this.authenticationStage = primaryStage;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/authentication.fxml"));
    loader.setControllerFactory(
        c -> new AuthenticationController(restClient, this::startProjectChooser));
    Parent root = loader.load();
    authenticationStage.setTitle("Coldab Login");
    Scene scene = new Scene(root);
    authenticationStage.setScene(scene);
    authenticationStage.show();
  }

  /**
   * open the project chooser ,if you picked a project it will open the editor.
   * you can open projects you are a collaborator/admin off , or create new ones.
   */
  private void startProjectChooser(Account account) {
    authenticationStage.hide();
    this.projectChooserStage = new Stage();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/projectChooser.fxml"));
    loader.setControllerFactory(c ->
        new ProjectChooserController(restClient, p -> startEditor(p, account)));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e, () -> "Exception while loading");
      throw new IllegalStateException("Could not load FXML");
    }
    projectChooserStage.setTitle("Coldab text");
    Scene scene = new Scene(root);
    projectChooserStage.setScene(scene);
    projectChooserStage.show();
  }

  /**
   * open the editor for the project you selected.
   * create /open files in the project, send chatmessage in the project chat.
   */
  private void startEditor(Project project, Account account) {
    projectChooserStage.hide();
    EditorController controller = new EditorController(project, account);
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editor.fxml"));
    loader.setControllerFactory(c -> controller);
    Parent root;
    try {
      root = loader.load();
    } catch (IOException e) {
      throw new IllegalStateException("Could not load FXML");
    }
    editorStage = new Stage();
    editorStage.setTitle(String.format("%s - Coldab text", project.getName()));
    Scene scene = new Scene(root);
    editorStage.setScene(scene);
    editorStage.show();
    WebSocketConnection webSocketConnection = new WebSocketConnection(
        project,
        restClient.getSessionId(),
        serverEndpoint -> {
          ChatComponent chatComponent = new ChatComponent(project.getChat(), serverEndpoint.chat());
          ProjectComponent projectComponent =
              new ProjectComponent(project, serverEndpoint.project(), account, controller);
          Platform.runLater(() ->
              controller.afterConnectionEstablished(chatComponent, projectComponent));
          return new WebSocketEndpoint(chatComponent, projectComponent);
        }, this::webSocketDisconnected);
    editorStage.setOnCloseRequest(event -> {
      webSocketConnection.disconnect();
      projectChooserStage.show();
    });
  }

  /**
   * Close editor when webSocket disconnects.
   */
  private void webSocketDisconnected() {
    Platform.runLater(() -> {
      LOGGER.warning("Close editor due to network error");
      editorStage.close();
      projectChooserStage.show();
    });
  }

  public static void main(String[] args) {
    launch(args);
  }
}
