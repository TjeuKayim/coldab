package com.github.coldab.server.ws;

import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.session.Caret;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.ProjectServer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
  private final Project project;
  private final List<ClientEndpoint> clients = new ArrayList<>();

  ProjectService(Project project) {
    this.project = project;
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

    public MessageReceiver(ClientEndpoint clientEndpoint) {
      this.clientEndpoint = clientEndpoint;
    }

    private Collection<ClientEndpoint> getOtherClients() {
      return clients.stream()
          .filter(ce -> ce != clientEndpoint)
          .collect(Collectors.toList());
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
      processEdit(fileId, addition);
    }

    @Override
    public void deletion(int fileId, Deletion deletion) {
      processEdit(fileId, deletion);
    }

    private void processEdit(int fileId, Edit edit) {
      List<Deletion> deletions = Collections.emptyList();
      List<Addition> additions = Collections.emptyList();
      if (edit instanceof Addition) {
        additions = Collections.singletonList((Addition) edit);
        clientEndpoint.project().confirmAddition(fileId, (Addition) edit);
      } else if (edit instanceof Deletion) {
        deletions = Collections.singletonList((Deletion) edit);
        clientEndpoint.project().confirmDeletion(fileId, (Deletion) edit);
      }
      for (ClientEndpoint client : getOtherClients()) {
        client.project().edits(fileId, additions, deletions);
      }
    }

    @Override
    public void files(List<TextFile> textFiles, List<BinaryFile> binaryFiles) {

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
