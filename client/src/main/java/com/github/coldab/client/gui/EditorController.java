package com.github.coldab.client.gui;

import com.github.coldab.client.gui.FileTree.DirectoryNode;
import com.github.coldab.client.project.ChatService;
import com.github.coldab.client.project.ProjectService;
import com.github.coldab.client.ws.WebSocketConnection;
import com.github.coldab.client.ws.WebSocketEndpoint;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.Chat;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.File;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import com.google.gson.Gson;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

public class EditorController implements Initializable {

  private final Project project;
  @FXML
  private TextField textFieldChatMessage;
  @FXML
  private Button btnChatMessage;
  @FXML
  private ListView<ChatMessage> chatPane;
  @FXML
  private VBox chatVBox;
  @FXML
  private TreeView<String> fileTreeView;
  @FXML
  private MenuItem menuOpenChat;

  private final Chat chat = new Chat();
  private Account account = new Account("Henkie", "henkie@gmail.com");
  private ChatService chatService;
  private ProjectService projectService;

  public EditorController(Project project) {
    this.project = project;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    new WebSocketConnection(project, serverEndpoint -> {
      chatService = new ChatService(chat, serverEndpoint.chat());
      projectService = new ProjectService(project, serverEndpoint.project(), account,
          this);
      Platform.runLater(this::afterConnectionEstablished);
      return new WebSocketEndpoint(chatService, projectService);
    });
  }

  private void afterConnectionEstablished() {
    updateFileTree();
    initChat();
  }

  private void initChat() {
    chat.addObserver(message -> Platform.runLater(() -> receiveChatMessage(message)));
    project.setChat(chat);
    menuOpenChat.setOnAction(this::toggleChat);
    btnChatMessage.setOnAction(this::btnChatMessagePressed);
  }

  private void receiveChatMessage(ChatMessage message) {
    chatPane.getItems().add(message);
  }

  private void btnChatMessagePressed(ActionEvent actionEvent) {
    String messageText = textFieldChatMessage.getText();
    if (messageText.length() < 1) return;
    ChatMessage message = new ChatMessage(messageText, account);
    chatService.sendMessage(message);
    textFieldChatMessage.setText("");
  }

  private void toggleChat(ActionEvent actionEvent) {
    if (chatVBox.getMaxWidth() == 0) {
      chatVBox.setMaxWidth(Double.MAX_VALUE);
    } else {
      chatVBox.setMaxWidth(0);
    }
  }

  private void updateFileTree() {
    // Create root
    TreeItem<String> rootItem = new TreeItem<>();

    // Test files
    LocalDateTime now = LocalDateTime.now();
    Collection<File> files = Arrays.asList(
        new TextFile("path/to/file.txt", now),
        new TextFile("path/to/another-file.txt", now),
        new TextFile("website/index.html", now)
    );

    DirectoryNode fileTree = FileTree.createFrom(files);

    // Add files
    addNodesToFileTree(rootItem, fileTree);

    fileTreeView.setShowRoot(false);
    fileTreeView.setRoot(rootItem);
  }

  private void addNodesToFileTree(TreeItem<String> treeItem, DirectoryNode fileTree) {
    for (FileTree child : fileTree.getChildren()) {
      TreeItem<String> childItem = new TreeItem<>(child.toString());
      treeItem.getChildren().add(childItem);
      if (child instanceof DirectoryNode) {
        // Directory
        childItem.expandedProperty().addListener((observable, oldValue, newValue) -> {
          FontAwesomeRegular iconCode =
              newValue ? FontAwesomeRegular.FOLDER_OPEN : FontAwesomeRegular.FOLDER;
          childItem.setGraphic(new FontIcon(iconCode));
        });
        childItem.setExpanded(true);
        addNodesToFileTree(childItem, (DirectoryNode) child);
      } else {
        // File
        childItem.setGraphic(new FontIcon(FontAwesomeRegular.FILE_ALT));
      }
    }
  }

  public void showAnnotation(Annotation annotation) {
    // FIXME: 7-5-2018 Update GUI
    System.out.println("Annotation: " + new Gson().toJson(annotation));
  }
}
