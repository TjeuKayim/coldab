package com.github.coldab.client.gui;

import com.github.coldab.client.gui.FileTree.DirectoryNode;
import com.github.coldab.client.project.ChatService;
import com.github.coldab.client.project.ProjectService;
import com.github.coldab.client.ws.WebSocketEndpoint;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.Chat;
import com.github.coldab.shared.chat.Chat.ChatObserver;
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
import java.util.List;
import java.util.ResourceBundle;
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

public class EditorController implements Initializable, ChatObserver {

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

  private Chat chat;
  private Account account;
  private ChatService chatService;
  private ProjectService projectService;

  public EditorController(Project project) {
    this.project = project;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    updateFileTree();
    initChat();
    WebSocketEndpoint webSocketEndpoint = new WebSocketEndpoint(project, account, this);
    chatService = webSocketEndpoint.chat();
    projectService = webSocketEndpoint.project();
  }

  private void initChat() {
    chat = new Chat();
    chat.addObserver(this);
    project.setChat(chat);
    menuOpenChat.setOnAction(this::toggleChat);
    btnChatMessage.setOnAction(this::btnChatMessagePressed);
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

  @Override
  public void chatUpdated(List<ChatMessage> messages) {
    chatPane.getItems().clear();
    for (ChatMessage message : messages) {
      chatPane.getItems().add(message);
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
