package com.github.coldab.shared.rest;

import java.io.InputStream;
import java.io.OutputStream;

public interface BinaryServer {

  /**
   * Send file from server to client.
   * @return true if successful
   */
  boolean download(int fileId, OutputStream stream);

  /**
   * Send file from client to server.
   * @return true if successful
   */
  boolean upload(int fileId, InputStream stream);
}
