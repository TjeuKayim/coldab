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
  private HashMap<Integer, TextFileObserver> textFileServices = new HashMap<>();

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
        .forEach(file -> project.getFiles().put(file.getId(), file));
  }

  @Override
  public void removeFile(int fileId) {
    project.getFiles().remove(fileId);
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
  public void annotations(int fileId, List<Annotation> annotations) {
    for (Annotation annotation : annotations) {
      textFileServices.get(fileId).newAnnotation(annotation);
    }
  }

  @Override
  public void caret(int fileId, Caret caret) {

  }

  public TextFileService openFile(TextFile file) {
    TextFileService textFileService = new TextFileService(file, account, new TextFileHandler(file),
        editorController);
    projectServer.subscribe(file.getId());
    textFileServices.put(file.getId(), textFileService);
    return textFileService;
  }

  public void closeFile(TextFile file) {
    projectServer.unsubscribe(file.getId());
  }

  /**
   * Passes messages from TextFileService to the server.
   */
  private class TextFileHandler implements TextFileObserver {

    private final TextFile file;

    public TextFileHandler(TextFile file) {
      this.file = file;
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
      projectServer.edits(file.getId(), additions, deletions);
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
