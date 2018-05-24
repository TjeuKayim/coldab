package com.github.coldab.client.gui;

import com.github.coldab.client.gui.FileTree.DirectoryNode;
import com.github.coldab.client.gui.FileTree.FileNode;
import com.github.coldab.client.project.ChatComponent;
import com.github.coldab.client.project.ProjectComponent;
import com.github.coldab.client.project.ProjectObserver;
import com.github.coldab.client.ws.WebSocketConnection;
import com.github.coldab.client.ws.WebSocketEndpoint;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.Chat;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.project.File;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

public class EditorController implements Initializable, ProjectObserver {

  private final Project project;
  @FXML
  private TabPane tabPane;
  @FXML
  private TextField textFieldChatMessage;
  @FXML
  private Button btnChatMessage;
  @FXML
  private ListView<ChatMessage> chatPane;
  @FXML
  private VBox chatVBox;
  @FXML
  private TreeView<FileTree> fileTreeView;
  @FXML
  private MenuItem menuOpenChat;

  private final Chat chat = new Chat();
  private Account account = new Account("HenkJan", "henk@jan.org");
  private ChatComponent chatComponent;
  private ProjectComponent projectComponent;

  public EditorController(Project project) {
    this.project = project;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //TODO: move this to another class (SOLID)
    new WebSocketConnection(project, serverEndpoint -> {
      chatComponent = new ChatComponent(chat, serverEndpoint.chat());
      projectComponent = new ProjectComponent(project, serverEndpoint.project(), account, this);
      Platform.runLater(this::afterConnectionEstablished);
      return new WebSocketEndpoint(chatComponent, projectComponent);
    });
  }

  private void afterConnectionEstablished() {
    updateFiles();
    initChat();
  }

  private void initChat() {
    chat.addObserver(this::receiveChatMessage);
    project.setChat(chat);
    menuOpenChat.setOnAction(this::toggleChat);
    btnChatMessage.setOnAction(this::btnChatMessagePressed);
  }

  private void receiveChatMessage(ChatMessage message) {
    Platform.runLater(() ->
        chatPane.getItems().add(message));
  }

  private void btnChatMessagePressed(ActionEvent actionEvent) {
    String messageText = textFieldChatMessage.getText();
    if (messageText.length() < 1) {
      return;
    }
    ChatMessage message = new ChatMessage(messageText, account);
    chatComponent.sendMessage(message);
    textFieldChatMessage.setText("");
  }

  private void toggleChat(ActionEvent actionEvent) {
    if (chatVBox.getMaxWidth() == 0) {
      chatVBox.setMaxWidth(Double.MAX_VALUE);
    } else {
      chatVBox.setMaxWidth(0);
    }
  }

  private void openFile(TextFile file) {
    Tab tab = new Tab();
    tabPane.getTabs().add(tab);
    TabController tabController = new TabController(file, tab, projectComponent);
  }

  @Override
  public void updateFiles() {
    Platform.runLater(() -> {
      // Create root
      TreeItem<FileTree> rootItem = new TreeItem<>();

      // Test files
      DirectoryNode fileTree = FileTree.createFrom(project.getFiles());

      // Add files
      addNodesToFileTree(rootItem, fileTree);

      fileTreeView.setShowRoot(false);
      fileTreeView.setRoot(rootItem);

      MenuItem openBtn = new MenuItem("Open in editor");
      openBtn.setOnAction(e -> {
        FileTree selected = fileTreeView.getSelectionModel().getSelectedItem().getValue();
        if (selected instanceof FileNode) {
          File file = ((FileNode) selected).getFile();
          if (file instanceof TextFile) {
            openFile(((TextFile) file));
          }
        }
      });
      ContextMenu menu = new ContextMenu(openBtn);
      fileTreeView.setContextMenu(menu);
    });
  }

  private void addNodesToFileTree(TreeItem<FileTree> treeItem, DirectoryNode fileTree) {
    for (FileTree child : fileTree.getChildren()) {
      TreeItem<FileTree> childItem = new TreeItem<>(child);
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

  @Override
  public void updateCollaborators() {

  }
}
