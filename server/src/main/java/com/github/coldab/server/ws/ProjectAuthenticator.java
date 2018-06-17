package com.github.coldab.server.ws;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import com.github.coldab.shared.project.Annotation;
import com.github.coldab.shared.project.BinaryFile;
import com.github.coldab.shared.project.TextFile;
import com.github.coldab.shared.session.Caret;
import com.github.coldab.shared.ws.ProjectServer;
import java.util.List;

/**
 * Wrapper to check permissions.
 *
 * <p>
 * I used IntelliJ's `Code|Delegate Methods`
 * </p>
 */
public class ProjectAuthenticator implements ProjectServer {

  private final ProjectServer server;
  private final Account account;

  public ProjectAuthenticator(ProjectServer server,
      Account account) {
    this.server = server;
    this.account = account;
  }


  @Override
  public void subscribe(int fileId) {
    server.subscribe(fileId);
  }

  @Override
  public void unsubscribe(int fileId) {
    server.unsubscribe(fileId);
  }

  @Override
  public void share(String email) {
    server.share(email);
  }

  @Override
  public void unshare(int accountId) {
    server.unshare(accountId);
  }

  @Override
  public void promote(int accountId) {
    server.promote(accountId);
  }

  @Override
  public void demote(int accountId) {
    server.demote(accountId);
  }

  @Override
  public void addition(int fileId, Addition addition) {
    if (addition.getAccount() != account) {

    }
    server.addition(fileId, addition);
  }

  @Override
  public void deletion(int fileId, Deletion deletion) {
    server.deletion(fileId, deletion);
  }

  @Override
  public void files(List<TextFile> textFiles,
      List<BinaryFile> binaryFiles) {
    server.files(textFiles, binaryFiles);
  }

  @Override
  public void removeFile(int fileId) {
    server.removeFile(fileId);
  }

  @Override
  public void annotations(int fileId,
      List<Annotation> annotations) {
    server.annotations(fileId, annotations);
  }

  @Override
  public void caret(int fileId, Caret caret) {
    server.caret(fileId, caret);
  }

}
