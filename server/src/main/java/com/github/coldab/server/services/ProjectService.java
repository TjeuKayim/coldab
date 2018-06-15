package com.github.coldab.server.services;

import com.github.coldab.server.dal.AccountStore;
import com.github.coldab.server.dal.FileStore;
import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.File;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ProjectServer;
import com.github.coldab.shared.ws.TextFileServer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Each project is managed by a ProjectService.
 */
public class ProjectService implements Service<ProjectServer, ProjectClient> {

  private final Project project;
  private final Map<ProjectClient, MessageReceiver> clients = new HashMap<>();
  private final Map<Integer, TextFileService> textFileServices = new HashMap<>();
  private final ProjectStore projectStore;
  private final FileStore fileStore;
  private final AccountStore accountStore;
  private static final Logger LOGGER = Logger.getLogger(ProjectService.class.getName());

  ProjectService(Project project, ProjectStore projectStore,
      FileStore fileStore, AccountStore accountStore) {
    this.project = project;
    this.projectStore = projectStore;
    this.fileStore = fileStore;
    this.accountStore = accountStore;
  }

  @Override
  public ProjectServer connect(ProjectClient projectClient, Account account) {
    MessageReceiver messageReceiver = new MessageReceiver(projectClient, account);
    clients.put(projectClient, messageReceiver);
    return messageReceiver;
  }

  @Override
  public void disconnect(ProjectClient projectClient) {
    clients.remove(projectClient)
        .unsubscribeAll();
  }

  private void notifyFiles(Collection<ProjectClient> clients, File... files) {
    List<TextFile> textFiles = new ArrayList<>();
    List<BinaryFile> binaryFiles = new ArrayList<>();
    for (File file : files) {
      if (file instanceof TextFile) {
        TextFile textFile = (TextFile) file;
        textFiles.add(textFile);
        textFileServices.computeIfAbsent(file.getId(),
            id -> new TextFileService(textFile, fileStore));
      } else if (file instanceof BinaryFile) {
        binaryFiles.add(((BinaryFile) file));
      }
    }
    if (textFiles.isEmpty() && binaryFiles.isEmpty()) {
      return;
    }
    // Notify clients
    for (ProjectClient projectClient : clients) {
      projectClient
          .files(textFiles.toArray(new TextFile[0]), binaryFiles.toArray(new BinaryFile[0]));
    }
  }

  private class MessageReceiver implements ProjectServer {

    private final ProjectClient client;
    private final Account account;
    private final Map<Integer, Subscription> subscriptions = new HashMap<>();

    public MessageReceiver(ProjectClient client, Account account) {
      this.client = client;
      this.account = account;
      notifyFiles(
          Collections.singletonList(client),
          project.getFiles().toArray(new File[0]));
    }

    public void unsubscribeAll() {
      subscriptions.forEach((fileId, subscription) ->
          textFileServices.get(fileId).disconnect(subscription.textFileClient));
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
      TextFileService textFileService = textFileServices.get(fileId);
      if (textFileService != null) {
        textFileService.disconnect(subscription.textFileClient);
      } else {
        LOGGER.info("Unsubscribe: file doesn't exist");
      }
    }

    @Override
    public void share(String email, boolean admin) {
      // Only admins can share
      if (!isAdmin()) {
        LOGGER.info("Non admin tries to share");
        return;
      }
      Account accountToShare = accountStore.findAccountByEmail(email);
      if (accountToShare == null) {
        LOGGER.info("account to share not found");
        return;
      }
      if (admin) {
        project.getAdmins().add(accountToShare);
      } else {
        project.getCollaborators().add(accountToShare);
      }
      projectStore.save(project);
    }

    @Override
    public void unshare(int accountId) {
      // Only admins can unshare
      if (!isAdmin()) {
        LOGGER.info("Non admin tries to unshare");
        return;
      }
      removeAccount(project.getCollaborators(), accountId);
      removeAccount(project.getAdmins(), accountId);
    }

    private boolean isAdmin() {
      return project.getAdmins().contains(account);
    }

    private Optional<Account> removeAccount(Collection<Account> collection, int accountId) {
      Optional<Account> optional = collection.stream().filter(a -> a.getId() == accountId)
          .findAny();
      optional.ifPresent(collection::remove);
      return optional;
    }

    @Override
    public void promote(int accountId) {
      // Only admins can promote someone
      if (!isAdmin()) {
        LOGGER.info("Non admin tries to promote someone");
        return;
      }
      Optional<Account> accountToPromote = removeAccount(project.getCollaborators(), accountId);
      if (!accountToPromote.isPresent()) {
        LOGGER.info("account to promote is not a collaborator");
        return;
      }
      project.getAdmins().add(accountToPromote.get());
    }

    @Override
    public void demote(int accountId) {
      // Only admins can demote someone
      if (!isAdmin()) {
        LOGGER.info("Non admin tries to promote someone");
        return;
      }
      Optional<Account> accountToDemote = removeAccount(project.getAdmins(), accountId);
      if (!accountToDemote.isPresent()) {
        LOGGER.info("account to demote is not an admin");
        return;
      }
      // Anti lockout
      if (project.getAdmins().size() == 1) {
        LOGGER.info("Cannot demote the only admin left");
        return;
      }
      project.getCollaborators().add(accountToDemote.get());
    }

    @Override
    public void addition(int fileId, Addition addition) {
      textFileServer(fileId).ifPresent(s -> s.newEdit(addition));
    }

    @Override
    public void deletion(int fileId, Deletion deletion) {
      textFileServer(fileId).ifPresent(s -> s.newEdit(deletion));
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

    private Optional<TextFileServer> textFileServer(int fileId) {
      return Optional.ofNullable(subscriptions.get(fileId))
          .map(s -> s.textFileServer);
    }

    private void file(File file) {
      // Update
      file = fileStore.save(file);
      project.getFiles().add(file);
      projectStore.save(project);
      // Notify all clients about it
      notifyFiles(clients.keySet(), file);
    }

    @Override
    public void removeFile(int fileId) {
      clients.forEach((c, receiver) -> {
        Subscription subscription = receiver.subscriptions.remove(fileId);
        if (subscription != null) {
          textFileServices.get(fileId).disconnect(subscription.textFileClient);
        }
        c.removeFile(fileId);
      });
      textFileServices.remove(fileId);
      project.getFiles().removeIf(f -> f.getId() == fileId);
      projectStore.save(project);
      fileStore.deleteById(fileId);
    }

  }
}
