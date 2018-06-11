package com.github.coldab.server.services;

import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.TextFileClient;
import java.util.Collections;
import java.util.List;

/**
 * TextFileForwarder converts TextFileClient messages into ProjectClient.
 * <p>
 * Converts messages and forward them to TextFileService.
 * </p>
 * <p>
 * I doubt whether its a good practice to have nested nested classes.
 * </p>
 */
class TextFileForwarder implements TextFileClient {

  private final ProjectClient client;
  private final int fileId;

  TextFileForwarder(ProjectClient client, int fileId) {
    this.client = client;
    this.fileId = fileId;
  }

  @Override
  public void newEdit(Edit edit) {
    List<Deletion> deletions = Collections.emptyList();
    List<Addition> additions = Collections.emptyList();
    if (edit instanceof Addition) {
      additions = Collections.singletonList((Addition) edit);
    } else if (edit instanceof Deletion) {
      deletions = Collections.singletonList((Deletion) edit);
    }
    client.edits(fileId, additions.toArray(new Addition[0]), deletions.toArray(new Deletion[0]));
  }

  @Override
  public void confirmEdit(Edit edit) {
    if (edit instanceof Addition) {
      Addition addition = (Addition) edit;
      client.confirmAddition(fileId, addition);
    } else if (edit instanceof Deletion) {
      Deletion deletion = (Deletion) edit;
      client.confirmDeletion(fileId, deletion);
    }
  }

  @Override
  public void newAnnotation(Annotation annotation) {

  }

  @Override
  public void updateTextFile(TextFile updatedFile) {

  }
}
