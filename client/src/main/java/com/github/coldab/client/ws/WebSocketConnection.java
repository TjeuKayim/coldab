package com.github.coldab.client.ws;

import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.MessageEncoder;
import com.github.coldab.shared.ws.ServerEndpoint;
import com.github.tjeukayim.socketinterface.SocketMessage;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.github.tjeukayim.socketinterface.SocketSender;
import java.io.IOException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketConnection extends TextWebSocketHandler {

  public static void main(String[] args) throws Exception {
    new WebSocketConnection("ws://localhost:8080/ws");
    Thread.sleep(3000);
  }

  private WebSocketSession session;
  private SocketReceiver socketReceiver;

  public WebSocketConnection(String url) {
    WebSocketClient client = new StandardWebSocketClient();
    WebSocketConnectionManager manager = new WebSocketConnectionManager(client, this, url);
    manager.start();
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    this.session = session;
    System.out.println("Connected WebSocket");
    this.session = session;
    ClientEndpoint clientEndpoint = SocketSender.create(ClientEndpoint.class, this::sendMessage);
    ServerEndpoint serverEndpoint = null;
    socketReceiver = new SocketReceiver(ServerEndpoint.class, serverEndpoint);
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    byte[] bytes = message.asBytes();
    MessageEncoder.decodeMessage(bytes, socketReceiver);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

  }

  private void sendMessage(SocketMessage socketMessage) {
    byte[] payload = MessageEncoder.encodeMessage(socketMessage);
    try {
      session.sendMessage(new TextMessage(payload));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}