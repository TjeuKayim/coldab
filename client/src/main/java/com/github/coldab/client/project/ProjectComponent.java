package com.github.coldab.client.project;

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
import com.github.coldab.shared.ws.TextFileClient;
import com.github.coldab.shared.ws.TextFileServer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class ProjectComponent implements ProjectClient {

  private final ProjectServer projectServer;
  private final Project project;
  private final Account account;
  private final ProjectObserver projectObserver;
  private HashMap<Integer, TextFileClient> textFileServices = new HashMap<>();

  public ProjectComponent(Project project, ProjectServer projectServer, Account account,
      ProjectObserver projectObserver) {
    this.project = project;
    this.projectServer = projectServer;
    this.account = account;
    this.projectObserver = projectObserver;
  }

  @Override
  public void collaborators(List<Account> admins, List<Account> collaborators) {
    project.getAdmins().clear();
    project.getAdmins().addAll(admins);
    project.getCollaborators().clear();
    project.getCollaborators().addAll(collaborators);
    projectObserver.updateCollaborators();
  }

  @Override
  public void files(TextFile[] textFiles, BinaryFile[] binaryFiles) {
    if (textFiles != null) {
      for (TextFile textFile : textFiles) {
        project.getFiles().add(textFile);
      }
    }
    if (binaryFiles != null) {
      for (BinaryFile binaryFile : binaryFiles) {
        project.getFiles().add(binaryFile);
      }
    }
    projectObserver.updateFiles();
  }

  @Override
  public void removeFile(int fileId) {
    project.getFiles().removeIf(f -> f.getId() == fileId);
  }

  @Override
  public void edits(int fileId, Addition[] additions, Deletion[] deletions) {
    Stream.concat(Arrays.stream(additions), Arrays.stream(deletions))
        .sorted(Comparator.comparingInt(Edit::getIndex))
        .forEach(e -> textFileServices.get(fileId).newEdit(e));
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

  public TextFileController openFile(TextFile file, TextFileObserver textFileObserver) {
    TextFileComponent textFileClient =
        new TextFileComponent(file, account, new TextFileHandler(file));
    textFileClient.addObserver(textFileObserver);
    projectServer.subscribe(file.getId());
    textFileServices.put(file.getId(), textFileClient);
    return textFileClient;
  }

  public void closeFile(TextFile file) {
    projectServer.unsubscribe(file.getId());
  }

  /**
   * Passes messages from TextFileComponent to the server.
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
      projectServer.files(new TextFile[]{updatedFile}, null);
    }
  }

}
