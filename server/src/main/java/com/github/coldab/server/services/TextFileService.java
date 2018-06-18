package com.github.coldab.server.services;

import com.github.coldab.server.dal.FileStore;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.ws.TextFileClient;
import com.github.coldab.shared.ws.TextFileServer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Every text-file is managed by a TextFileService.
 */
public class TextFileService implements Service<TextFileServer, TextFileClient> {

  private TextFile file;
  private final FileStore fileStore;
  private final List<TextFileClient> clients = new ArrayList<>();
  private static final Logger LOGGER = Logger.getLogger(TextFileService.class.getName());

  public TextFileService(TextFile file, FileStore fileStore) {
    this.file = file;
    this.fileStore = fileStore;
  }

  @Override
  public TextFileServer connect(TextFileClient client, Account account) {
    clients.add(client);
    return new MessageHandler(client, account);
  }

  @Override
  public void disconnect(TextFileClient client) {
    clients.remove(client);
  }

  private class MessageHandler implements TextFileServer {
    private final TextFileClient client;
    private final Account account;

    /**
     * Maps local indices to global indices.
     */
    private final Map<Integer, Integer> localIndices = new HashMap<>();

    private MessageHandler(TextFileClient client, Account account) {
      this.client = client;
      this.account = account;
      sendAllEdits();
    }

    private void sendAllEdits() {
      // todo: Send edits all at once
      for (Edit edit : file.getEdits()) {
        client.newEdit(edit);
      }
    }

    @Override
    public void newEdit(Edit edit) {
      // Check author
      if (!account.equals(edit.getAccount())) {
        LOGGER.severe("Edit has invalid author");
        return;
      }
      edit.setAccount(account);
      int localIndex = edit.getIndex();
      file.confirmEdit(edit, localIndices);
      localIndices.put(localIndex, edit.getIndex());
      client.confirmEdit(edit);
      notifyOthers(c -> c.newEdit(edit));
      while (true) {
        try {
          file = fileStore.save(file);
          break;
        } catch (ConcurrentModificationException e) {
          LOGGER.severe("ConcurrentModificationException while saving edit");
          LOGGER.info(e.toString());
        }
      }
    }

    @Override
    public void updateTextFile(TextFile updatedFile) {

    }

    private void notifyOthers(Consumer<TextFileClient> message) {
      clients.stream()
          .filter(ce -> ce != client)
          .forEach(message);
    }
  }
}
