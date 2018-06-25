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
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class EditorController implements ProjectObserver {

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
  @FXML
  private MenuItem newProject;

  private final Project project;
  private final Account account;

  private ChatController chatController;

  private ProjectController projectController;
  private final HashMap<TextFile, TabController> tabs = new HashMap<>();

  public EditorController(Project project, Account account) {
    this.project = project;
    this.account = account;
  }

  public void afterConnectionEstablished(ChatController chatController,
      ProjectController projectController) {
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

  /**
   * methode that gets called of the there is a new chat message.
   */
  private void receiveChatMessage(ChatMessage message) {
    Platform.runLater(() ->
        chatPane.getItems().add(message));
  }

  /**
   * send a new chatmessage, to the server.
   */
  private void btnChatMessagePressed(ActionEvent actionEvent) {
    String messageText = textFieldChatMessage.getText();
    if (messageText.length() < 1) {
      return;
    }
    ChatMessage message = new ChatMessage(messageText, account);
    chatController.sendMessage(message);
    textFieldChatMessage.setText("");
  }

  /**
   *  hide or show the chat element in the editor
   */
  private void toggleChat(ActionEvent actionEvent) {
    if (chatVBox.getMaxWidth() == 0) {
      chatVBox.setMaxWidth(Double.MAX_VALUE);
    } else {
      chatVBox.setMaxWidth(0);
    }
  }

  /**
   * open a file from the project in the editor.
   */
  private void openFile(TextFile file) {
    Tab tab = new Tab();
    tabPane.getTabs().add(tab);
    tabs.put(file, new TabController(file, tab, projectController));
  }


  /**
   * refresh the  list of files on the project.
   */
  @Override
  public void updateFiles() {
    Platform.runLater(() -> {
      // Close tabs for removed files
      project.getFiles().stream()
          .map(tabs::get)
          .filter(Objects::nonNull)
          .forEach(TabController::fileDeleted);

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

  /**
   *  create a new file inside the project
   */
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

  /**
   * share the current project with a diffrent user using there email address.
   */
  public void share(ActionEvent actionEvent) {
    TextInputDialog dialog = new TextInputDialog("");
    dialog.setTitle("Share");
    dialog.setHeaderText("Invite someone");
    dialog.setContentText("E-mail");
    CheckBox checkBox = new CheckBox("Give admin privileges");
    checkBox.setSelected(true);
    dialog.getDialogPane().getChildren().add(checkBox);
    Optional<String> result = dialog.showAndWait();
    result.ifPresent(email -> projectController.share(email, checkBox.isSelected()));
  }
}
