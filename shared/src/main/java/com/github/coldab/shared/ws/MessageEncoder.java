package com.github.coldab.shared.ws;

import com.github.tjeukayim.socketinterface.SocketMessage;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Encode and decodes SocketMessages to JSON.
 *
 * <p>
 *   This class makes use of
 *   <a href="https://github.com/TjeuKayim/socket-interface">
 *     com.github.tjeukayim.socketinterface
 *   </a>
 * </p>
 *
 * <pre>
 * <code>
 * [
 *   "interface",
 *   "method",
 *   "arg1",
 *   "arg2"
 * ]
 * </code>
 * </pre>
 */
public class MessageEncoder {

  private static final Gson gson = new Gson();

  /**
   * Encodes message to JSON and returns bytes.
   */
  public static byte[] encodeMessage(SocketMessage socketMessage) {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(socketMessage.getEndpoint());
    jsonArray.add(socketMessage.getMethod());
    for (Object argument : socketMessage.getArguments()) {
      jsonArray.add(gson.toJsonTree(argument));
    }
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    gson.toJson(jsonArray, new OutputStreamWriter(outputStream));
    return outputStream.toByteArray();
  }

  /**
   * Decodes bytes from JSON to a SocketMessage.
   */
  public static void decodeMessage(byte[] bytes, SocketReceiver receiver) {
    // Read bytes
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    JsonElement json = gson.fromJson(new InputStreamReader(inputStream), JsonElement.class);
    // Parse json array
    JsonArray jsonArray = json.getAsJsonArray();
    String endpoint = jsonArray.get(0).getAsString();
    String method = jsonArray.get(1).getAsString();
    SocketMessage socketMessage = new SocketMessage(endpoint, method);
    receiver.receive(socketMessage, classes -> {
      // Parse arguments
      Object[] arguments = new Object[classes.length];
      for (int i = 0; i < classes.length; i++) {
        JsonElement element = jsonArray.get(i + 2);
        arguments[i] = gson.fromJson(element, classes[i]);
      }
      return arguments;
    });
  }
}
