package com.github.coldab.shared.ws;

import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;

public interface ProjectServer extends UpdateSender {

  /**
   * Subscribe a user to a file.
   * @param fileId the id of the file the user subscribes to.
   */
  void subscribe(int fileId);

  /**
   * Unsubscribe a user from a file.
   * @param fileId the id of the file the user wants to unsubscribe from.
   */

  void unsubscribe(int fileId);

  /**
   * Share a project with a user using there email.
   * @param email the emailadress of the user that receives access to the project.
   */

  void share(String email);

  /**
   * unshare a project with a user, using there id.
   * @param accountId the id of the user that you don't want to have access to the project any more.
   */

  void unshare(int accountId);

  /**
   * Change a user from collaborator to admin.
   * @param accountId the id of the user you want to promote.
   */

  void promote(int accountId);

  /**
   * Change a user from Admin to collaborator
   * @param accountId the id of the user you want to demote.
   */

  void demote(int accountId);

  /**
   *  Add a addition to a file.
   * @param fileId the id of the file.
   * @param addition the addition.
   */

  void addition(int fileId, Addition addition);

  /**
   * Add a deletion to a file.
   * @param fileId the id of the file.
   * @param deletion the deletion.
   */

  void deletion(int fileId, Deletion deletion);
}
