package com.github.coldab.client.project;

import com.github.coldab.shared.project.File;
import com.github.coldab.shared.project.TextFile;

public interface ProjectController {

  /**
   * open a file in the project.
   * @param file the file you want to open.
   * @param textFileObserver a textfileObserver.
   * @return a object that implements the TextfileControler interface.
   */

  TextFileController openFile(TextFile file, TextFileObserver textFileObserver);

  /**
   * close a file that is currently open.
   * @param file the file you want to close.
   */
  void closeFile(TextFile file);

  /**
   * create a new file and add it to the project.
   * @param file the new file you want to create.
   */
  void createFile(File file);

  /**
   * delete a file fand remove it from the project.
   * @param file the file you want to delete.
   */
  void deleteFile(File file);

  /**
   * share a project with another user.
   * @param email the email address of the user you want to share the project with
   * @param admin a bolean that indicates if the user should be an admin.
   */
  void share(String email, boolean admin);
}
