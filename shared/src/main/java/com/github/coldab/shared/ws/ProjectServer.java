package com.github.coldab.shared.ws;

public interface ProjectServer extends UpdateSender {

  void subscribe(int fileId);

  void unsubscribe(int fileId);

  void share(String email);

  void unshare(int accountId);

  void promote(int accountId);

  void demote(int accountId);
}
