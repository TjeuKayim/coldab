package com.github.coldab.server.services;

import com.github.coldab.shared.ws.TextFileClient;
import com.github.coldab.shared.ws.TextFileServer;

class Subscription {
  final TextFileServer textFileServer;
  final TextFileClient textFileClient;

  public Subscription(TextFileServer textFileServer, TextFileClient textFileClient) {
    this.textFileServer = textFileServer;
    this.textFileClient = textFileClient;
  }
}
