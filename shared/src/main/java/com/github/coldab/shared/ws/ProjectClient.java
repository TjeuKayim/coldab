package com.github.coldab.shared.ws;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;
import java.util.List;

public interface ProjectClient extends UpdateSender {

  void edits(int fileId, Addition[] additions, Deletion[] deletions);

  /**
   * An edit gets send back to the author as confirmation.
   */
  void confirmAddition(int fileId, Addition addition);

  /**
   * An edit gets send back to the author as confirmation.
   */
  void confirmDeletion(int fileId, Deletion deletion);

  void collaborators(List<Account> admins, List<Account> collaborators);
}
