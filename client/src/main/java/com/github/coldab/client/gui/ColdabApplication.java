package com.github.coldab.client.gui;

import com.github.coldab.client.project.ChatComponent;
import com.github.coldab.client.project.ProjectComponent;
import com.github.coldab.client.rest.RestClient;
import com.github.coldab.client.ws.WebSocketConnection;
import com.github.coldab.client.ws.WebSocketEndpoint;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.rest.AccountServer;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ColdabApplication extends Application {

  private Stage projectChooserStage;
  private Stage authenticationStage;

  @Override
  public void start(Stage primaryStage) throws IOException {
    this.authenticationStage = primaryStage;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/authentication.fxml"));
    AccountServer accountServer = new RestClient();
    loader.setControllerFactory(c -> new AuthenticationController());

    Parent root = loader.load();
    authenticationStage.setTitle("Coldab Login");
    Scene scene = new Scene(root);
    authenticationStage.setScene(scene);
    authenticationStage.show();

/*    this.projectChooserStage = primaryStage;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/projectChooser.fxml"));
    AccountServer accountServer = new RestClient();
    Account account = new Account("HenkJan", "henk@jan.org", "1234");
    loader.setControllerFactory(c ->
        new ProjectChooserController(accountServer, p -> startEditor(p, account)));
    Parent root = loader.load();
    projectChooserStage.setTitle("Coldab text");
    Scene scene = new Scene(root);
    projectChooserStage.setScene(scene);
    projectChooserStage.show();*/
  }

  private void startEditor(Project project, Account todo) {
    //todo: Use account parameter
    Account account = project.getAdmins().iterator().next();

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
    Stage stage = new Stage();
    stage.setTitle(String.format("%s - Coldab text", project.getName()));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    WebSocketConnection webSocketConnection = new WebSocketConnection(project, serverEndpoint -> {
      ChatComponent chatComponent = new ChatComponent(project.getChat(), serverEndpoint.chat());
      ProjectComponent projectComponent =
          new ProjectComponent(project, serverEndpoint.project(), account, controller);
      Platform.runLater(() ->
          controller.afterConnectionEstablished(chatComponent, projectComponent));
      return new WebSocketEndpoint(chatComponent, projectComponent);
    });
    stage.setOnCloseRequest(event -> {
      webSocketConnection.disconnect();
      projectChooserStage.show();
    });
  }

  public static void main(String[] args) {
    launch(args);
  }
}
