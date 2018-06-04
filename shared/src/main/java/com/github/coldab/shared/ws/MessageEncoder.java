package com.github.coldab.shared.ws;

import com.github.tjeukayim.socketinterface.SocketMessage;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Encode and decodes SocketMessages to JSON.
 *
 * <p>
 * This class makes use of
 * <a href="https://github.com/TjeuKayim/socket-interface">
 * com.github.tjeukayim.socketinterface
 * </a>
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

  private static final Logger LOGGER = Logger.getLogger(MessageEncoder.class.getName());

  private MessageEncoder() {
    throw new IllegalStateException("Utility class");
  }

  private static final Gson gson = createGson();

  private static Gson createGson() {
    return new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();
  }

  public static Gson getGson() {
    return gson;
  }

  /**
   * Encodes message to JSON and returns bytes.
   */
  public static byte[] encodeMessage(SocketMessage socketMessage) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream))
        .beginArray()
        .value(socketMessage.getEndpoint())
        .value(socketMessage.getMethod());
    for (Object argument : socketMessage.getArguments()) {
      writer.jsonValue(gson.toJson(argument));
    }
    writer.endArray().close();
    byte[] bytes = outputStream.toByteArray();
    LOGGER.finer(() -> "Sending message: " + new String(bytes));
    return bytes;
  }

  /**
   * Decodes bytes from JSON to a SocketMessage.
   */
  public static void decodeMessage(byte[] bytes, SocketReceiver receiver) {
    LOGGER.finer(() -> "Received message: " + new String(bytes));
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

  public static void enableDebugging() {
    LOGGER.setLevel(Level.FINER);
    LOGGER.fine("WebSocket Debugging enabled");
  }

  private static class LocalDateTimeAdapter implements
      JsonSerializer<LocalDateTime>,
      JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT,
        JsonDeserializationContext context) {
      return LocalDateTime.parse(json.getAsString());
    }

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc,
        JsonSerializationContext context) {
      return new JsonPrimitive(src.toString());
    }
  }
}
