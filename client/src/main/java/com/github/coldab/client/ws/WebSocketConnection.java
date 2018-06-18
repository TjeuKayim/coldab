package com.github.coldab.client.ws;

import com.github.coldab.client.Main;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.MessageEncoder;
import com.github.coldab.shared.ws.ServerEndpoint;
import com.github.tjeukayim.socketinterface.SocketMessage;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.github.tjeukayim.socketinterface.SocketSender;
import java.io.IOException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketConnection extends TextWebSocketHandler {

  private WebSocketSession session;
  private SocketReceiver socketReceiver;
  private final Function<ServerEndpoint, ClientEndpoint> endpointFactory;
  private final Runnable disconnectedCallback;
  private static final Logger LOGGER = Logger.getLogger(WebSocketConnection.class.getName());
  private final WebSocketConnectionManager manager;

  public WebSocketConnection(Project project, String sessionId, Function<ServerEndpoint,
      ClientEndpoint> endpointFactory, Runnable disconnectedCallback) {
    this.endpointFactory = endpointFactory;
    this.disconnectedCallback = disconnectedCallback;
    WebSocketClient client = new StandardWebSocketClient();
    String url = Main.getWebSocketEndpoint() + project.getId();
    manager = new WebSocketConnectionManager(client, this, url);
    manager.getHeaders().add("Session", sessionId);
    manager.start();
    LOGGER.info("Connecting to WebSocket");
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    LOGGER.info("WebSocket connection established");
    this.session = session;
    ServerEndpoint serverEndpoint = SocketSender.create(ServerEndpoint.class, this::sendMessage);
    ClientEndpoint clientEndpoint = endpointFactory.apply(serverEndpoint);
    socketReceiver = new SocketReceiver(ClientEndpoint.class, clientEndpoint);
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    byte[] bytes = message.asBytes();
    MessageEncoder.decodeMessage(bytes, socketReceiver);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    LOGGER.log(Level.INFO, "WebSocket disconnected, status: {0}", status);
    this.session = null;
    disconnectedCallback.run();
  }

  public void disconnect() {
    manager.stop();
  }

  private void sendMessage(SocketMessage socketMessage) {
    byte[] payload = new byte[0];
    try {
      payload = MessageEncoder.encodeMessage(socketMessage);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.toString(), e);
    }
    try {
      session.sendMessage(new TextMessage(payload));
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.toString(), e);
    }
  }
}