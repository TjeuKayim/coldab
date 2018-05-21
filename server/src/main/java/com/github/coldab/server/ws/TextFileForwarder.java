package com.github.coldab.server.ws;

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
 * Converts messages and forward them to TextFileService.
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
    client.edits(fileId, additions, deletions);
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
