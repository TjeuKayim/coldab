package com.github.coldab.server.ws;

import com.github.coldab.server.dal.FileStore;
import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.File;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.session.Caret;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ProjectServer;
import com.github.coldab.shared.ws.TextFileClient;
import com.github.coldab.shared.ws.TextFileServer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Each project is managed by a ProjectService.
 */
public class ProjectService implements Service<ProjectServer, ProjectClient> {

  private final Project project;
  private final List<ProjectClient> clients = new ArrayList<>();
  private final Map<Integer, TextFileService> textFileServices = new HashMap<>();
  private final ProjectStore projectStore;
  private final FileStore fileStore;

  ProjectService(Project project, ProjectStore projectStore,
      FileStore fileStore) {
    this.project = project;
    this.projectStore = projectStore;
    this.fileStore = fileStore;
  }

  @Override
  public ProjectServer connect(ProjectClient projectClient, Account account) {
    clients.add(projectClient);
    return new MessageReceiver(projectClient, account);
  }

  @Override
  public void disconnect(ProjectClient projectClient) {
    clients.remove(projectClient);
  }

  public List<ProjectClient> getClients() {
    return Collections.unmodifiableList(clients);
  }

  private class MessageReceiver implements ProjectServer {

    private final ProjectClient client;
    private final Account account;
    private final Map<Integer, Subscription> subscriptions = new HashMap<>();

    public MessageReceiver(ProjectClient client, Account account) {
      this.client = client;
      this.account = account;
      filesUpdated(project.getFiles().stream().toArray(File[]::new));
    }

    @Override
    public void subscribe(int fileId) {
      TextFileForwarder forwarder = new TextFileForwarder(client, fileId);
      TextFileService textFileService = textFileServices.get(fileId);
      TextFileServer server =
          textFileService.connect(forwarder, account);
      subscriptions.put(fileId, new Subscription(server, forwarder));
    }

    @Override
    public void unsubscribe(int fileId) {
      Subscription subscription = subscriptions.remove(fileId);
      textFileServices.get(fileId).disconnect(subscription.textFileClient);
    }

    @Override
    public void share(String email) {

    }

    @Override
    public void unshare(int accountId) {

    }

    @Override
    public void promote(int accountId) {

    }

    @Override
    public void demote(int accountId) {

    }

    @Override
    public void addition(int fileId, Addition addition) {
      textFileClient(fileId).newEdit(addition);
    }

    @Override
    public void deletion(int fileId, Deletion deletion) {
      textFileClient(fileId).newEdit(deletion);
    }

    @Override
    public void files(TextFile[] textFiles, BinaryFile[] binaryFiles) {
      if (textFiles != null) {
        for (TextFile textFile : textFiles) {
          file(textFile);
        }
      }
      if (binaryFiles != null) {
        for (BinaryFile binaryFile : binaryFiles) {
          file(binaryFile);
        }
      }
    }

    private TextFileClient textFileClient(int fileId) {
      return subscriptions.get(fileId).textFileClient;
    }

    private void file(File file) {
      // Update
      file = fileStore.save(file);
      project.getFiles().add(file);
      // Notify all clients about it
      filesUpdated(file);
    }

    private void filesUpdated(File... files) {
      List<TextFile> textFiles = new ArrayList<>();
      List<BinaryFile> binaryFiles = new ArrayList<>();
      for (File file : files) {
        if (file instanceof TextFile) {
          TextFile textFile = (TextFile) file;
          textFiles.add(textFile);
          textFileServices.computeIfAbsent(file.getId(),
              id -> new TextFileService(textFile));
        } else if (file instanceof BinaryFile) {
          binaryFiles.add(((BinaryFile) file));
        }
      }
      // Notify clients
      for (ProjectClient projectClient : clients) {
        projectClient.files(textFiles.toArray(new TextFile[0]), binaryFiles.toArray(new BinaryFile[0]));
      }
    }

    @Override
    public void removeFile(int fileId) {

    }

    @Override
    public void annotations(int fileId, List<Annotation> annotations) {

    }

    @Override
    public void caret(int fileId, Caret caret) {

    }
  }
}
