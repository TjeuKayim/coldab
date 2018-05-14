package com.github.coldab.shared.ws;

import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.edit.Deletion;

public interface ProjectServer extends UpdateSender {

  void subscribe(int fileId);

  void unsubscribe(int fileId);

  void share(String email);

  void unshare(int accountId);

  void promote(int accountId);

  void demote(int accountId);

  void addition(int fileId, Addition addition);

  void deletion(int fileId, Deletion deletion);
}
