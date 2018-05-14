package com.github.coldab.client.project;

import com.github.coldab.client.gui.EditorController;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.edit.Edit;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.session.Caret;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ProjectServer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class ProjectService implements ProjectClient {

  private final ProjectServer projectServer;
  private final Project project;
  private final Account account;
  private final EditorController editorController;
  private HashMap<Integer, TextFileClient> textFileServices = new HashMap<>();

  public ProjectService(Project project, ProjectServer projectServer,
      Account account, EditorController editorController) {
    this.project = project;
    this.projectServer = projectServer;
    this.account = account;
    this.editorController = editorController;
  }

  @Override
  public void collaborators(List<Account> admins, List<Account> collaborators) {

  }

  @Override
  public void files(List<TextFile> textFiles, List<BinaryFile> binaryFiles) {
    Stream.concat(textFiles.stream(), binaryFiles.stream())
        .forEach(file -> project.getFilesById().put(file.getId(), file));
  }

  @Override
  public void removeFile(int fileId) {
    project.getFilesById().remove(fileId);
  }

  @Override
  public void edits(int fileId, List<Addition> additions, List<Deletion> deletions) {
    for (Addition addition : additions) {
      textFileServices.get(fileId).newEdit(addition);
    }
    for (Deletion deletion : deletions) {
      textFileServices.get(fileId).newEdit(deletion);
    }
  }

  @Override
  public void confirmAddition(int fileId, Addition addition) {
    textFileServices.get(fileId).confirmEdit(addition);
  }

  @Override
  public void confirmDeletion(int fileId, Deletion deletion) {
    textFileServices.get(fileId).confirmEdit(deletion);
  }

  @Override
  public void annotations(int fileId, List<Annotation> annotations) {
    for (Annotation annotation : annotations) {
      textFileServices.get(fileId).newAnnotation(annotation);
    }
  }

  @Override
  public void caret(int fileId, Caret caret) {

  }

  public TextFileClient openFile(TextFile file) {
    TextFileClient textFileClient = new TextFileService(file, account, new TextFileHandler(file),
        editorController);
    projectServer.subscribe(file.getId());
    textFileServices.put(file.getId(), textFileClient);
    return textFileClient;
  }

  public void closeFile(TextFile file) {
    projectServer.unsubscribe(file.getId());
  }

  /**
   * Passes messages from TextFileService to the server.
   */
  private class TextFileHandler implements TextFileServer {

    private final TextFile file;

    public TextFileHandler(TextFile file) {
      this.file = file;
    }

    @Override
    public void newEdit(Edit edit) {
      if (edit instanceof Addition) {
        projectServer.addition(file.getId(), (Addition) edit);
      } else if (edit instanceof Deletion) {
        projectServer.deletion(file.getId(), (Deletion) edit);
      }
    }

    @Override
    public void newAnnotation(Annotation annotation) {
      projectServer.annotations(file.getId(), Collections.singletonList(annotation));
    }

    @Override
    public void updateTextFile(TextFile updatedFile) {
      projectServer.files(Collections.singletonList(updatedFile), Collections.emptyList());
    }
  }

}
