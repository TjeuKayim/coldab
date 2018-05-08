package com.github.coldab.client.ws;

import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.MessageEncoder;
import com.github.coldab.shared.ws.ServerEndpoint;
import com.github.tjeukayim.socketinterface.SocketMessage;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.github.tjeukayim.socketinterface.SocketSender;
import java.io.IOException;
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

  public static final String WS_ENDPOINT = "ws://localhost:8080/ws/";
  private WebSocketSession session;
  private SocketReceiver socketReceiver;
  private final ClientEndpoint clientEndpoint;
  private ServerEndpoint serverEndpoint;
  private static final Logger LOGGER = Logger.getLogger(WebSocketConnection.class.getName());

  public WebSocketConnection(int projectId, ClientEndpoint clientEndpoint) {
    this.clientEndpoint = clientEndpoint;
    WebSocketClient client = new StandardWebSocketClient();
    String url = WS_ENDPOINT + projectId;
    WebSocketConnectionManager manager = new WebSocketConnectionManager(client, this, url);
    manager.start();
    // TODO: 7-5-2018 Sluit de connectie af nadat het project is gesloten
  }

  public ServerEndpoint getServerEndpoint() {
    return serverEndpoint;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    this.session = session;
    LOGGER.fine("WebSocket connection established");
    serverEndpoint = SocketSender.create(ServerEndpoint.class, this::sendMessage);
    socketReceiver = new SocketReceiver(ClientEndpoint.class, clientEndpoint);
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    byte[] bytes = message.asBytes();
    MessageEncoder.decodeMessage(bytes, socketReceiver);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    this.session = null;
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