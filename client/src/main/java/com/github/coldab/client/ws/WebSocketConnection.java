package com.github.coldab.client.ws;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketConnection {

  public static void main(String[] args) throws Exception {
    WebSocketClient client = new StandardWebSocketClient();
    WebSocketHandler handler = new SocketHandler();
    String url = "ws://localhost:8080/ws";
    WebSocketConnectionManager manager = new WebSocketConnectionManager(client, handler, url);
    manager.start();
    Thread.sleep(3000);
  }

  public static class SocketHandler extends TextWebSocketHandler {

    private WebSocketSession session;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
      System.out.println(message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
      this.session = session;
      session.sendMessage(new TextMessage("Hello World"));
    }
  }
}