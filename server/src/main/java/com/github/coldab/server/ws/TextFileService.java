package com.github.coldab.server.ws;

import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.ws.ClientEndpoint;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TextFileService {

  private final TextFile file;
  private final ClientEndpoint clientEndpoint;
  private final List<ClientEndpoint> clients;
  private final Map<Integer, Integer> localIndices = new HashMap<>();

  public TextFileService(TextFile file, ClientEndpoint clientEndpoint,
      List<ClientEndpoint> clients) {
    this.file = file;
    this.clientEndpoint = clientEndpoint;
    this.clients = clients;
  }

  public void processEdit(Edit edit) {
    List<Deletion> deletions = Collections.emptyList();
    List<Addition> additions = Collections.emptyList();
    if (edit instanceof Addition) {
      additions = Collections.singletonList((Addition) edit);
      clientEndpoint.project().confirmAddition(file.getId(), (Addition) edit);
    } else if (edit instanceof Deletion) {
      deletions = Collections.singletonList((Deletion) edit);
      clientEndpoint.project().confirmDeletion(file.getId(), (Deletion) edit);
    }
    for (ClientEndpoint client : getOtherClients()) {
      client.project().edits(file.getId(), additions, deletions);
    }
  }

  private Collection<ClientEndpoint> getOtherClients() {
    return clients.stream()
        .filter(ce -> ce != clientEndpoint)
        .collect(Collectors.toList());
  }
}
