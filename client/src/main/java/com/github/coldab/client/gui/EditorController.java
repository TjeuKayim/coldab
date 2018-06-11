package com.github.coldab.client.gui;

import com.github.coldab.client.gui.FileTree.DirectoryNode;
import com.github.coldab.client.gui.FileTree.FileNode;
import com.github.coldab.client.project.ChatController;
import com.github.coldab.client.project.ProjectController;
import com.github.coldab.client.project.ProjectObserver;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.project.File;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

public class EditorController implements Initializable, ProjectObserver {

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

  private final Project project;
  private final Account account;

  private ChatController chatController;

  private ProjectController projectController;

  public EditorController(Project project, Account account) {
    this.project = project;
    this.account = account;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  public void afterConnectionEstablished(ChatController chatController, ProjectController projectController) {
    this.chatController = chatController;
    this.projectController = projectController;
    updateFiles();
    initChat();
  }

  private void initChat() {
    project.getChat().addObserver(this::receiveChatMessage);
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
    chatController.sendMessage(message);
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
    new TabController(file, tab, projectController);
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
      MenuItem removeBtn = new MenuItem("Remove file");
      removeBtn.setOnAction(event -> {
        FileTree selected = fileTreeView.getSelectionModel().getSelectedItem().getValue();
        if (selected instanceof FileNode) {
          File file = ((FileNode) selected).getFile();
          projectController.deleteFile(file);
        }
      });
      ContextMenu menu = new ContextMenu(openBtn, removeBtn);
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

  public void newFile(ActionEvent actionEvent) {
    TextInputDialog dialog = new TextInputDialog("");
    dialog.setTitle("New file");
    dialog.setHeaderText("New file");
    dialog.setContentText("Filename:");
    Optional<String> result = dialog.showAndWait();
    result.ifPresent(fileName -> {
      if (fileName.isEmpty()) {
        return;
      }
      projectController.createFile(new TextFile(0, fileName));
    });
  }
}
