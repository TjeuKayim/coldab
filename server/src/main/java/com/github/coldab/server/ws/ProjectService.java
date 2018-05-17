package com.github.coldab.server.ws;

import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.File;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.session.Caret;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.ProjectServer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ProjectService {

  private final Project project;
  private final List<ClientEndpoint> clients = new ArrayList<>();
  private final ProjectStore store;

  ProjectService(Project project, ProjectStore store) {
    this.project = project;
    this.store = store;
  }

  public ProjectServer addClient(ClientEndpoint clientEndpoint) {
    clients.add(clientEndpoint);
    return new MessageReceiver(clientEndpoint);
  }

  public void removeClient(ClientEndpoint clientEndpoint) {
    clients.remove(clientEndpoint);
  }

  public List<ClientEndpoint> getClients() {
    return Collections.unmodifiableList(clients);
  }

  private class MessageReceiver implements ProjectServer {

    private final ClientEndpoint clientEndpoint;
    private final Map<Integer, TextFileService> textFileServices = new HashMap<>();

    public MessageReceiver(ClientEndpoint clientEndpoint) {
      this.clientEndpoint = clientEndpoint;
    }

    @Override
    public void subscribe(int fileId) {

    }

    @Override
    public void unsubscribe(int fileId) {

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
      textFileServices.get(fileId).processEdit(addition);
    }

    @Override
    public void deletion(int fileId, Deletion deletion) {
      textFileServices.get(fileId).processEdit(deletion);
    }

    @Override
    public void files(List<TextFile> textFiles, List<BinaryFile> binaryFiles) {
      Stream.concat(textFiles.stream(), binaryFiles.stream())
          .forEach(this::file);
    }

    private void file(File file) {
      if (file.getId() == 0) {
        // Create new
        project.getFiles().add(file);
      } else {
        // Update
        project.getFilesById().replace(file.getId(), file);
      }
      // Save to db and apply changes
      project.setFiles(store.save(project).getFiles());
      File updated = project.getFilesById().get(file.getId());
      // Notify all clients about it
      filesUpdated(updated);
    }

    private void filesUpdated(File... files) {
      List<TextFile> textFiles = new ArrayList<>();
      List<BinaryFile> binaryFiles = new ArrayList<>();
      for (File file : files) {
        if (file instanceof TextFile) {
          TextFile textFile = (TextFile) file;
          textFiles.add(textFile);
          textFileServices.computeIfAbsent(file.getId(),
              id -> new TextFileService(textFile, clientEndpoint, clients));
        } else if (file instanceof BinaryFile) {
          binaryFiles.add(((BinaryFile) file));
        }
      }
      // Notify clients
      for (ClientEndpoint client : clients) {
        client.project().files(textFiles, binaryFiles);
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
