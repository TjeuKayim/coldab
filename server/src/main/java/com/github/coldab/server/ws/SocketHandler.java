package com.github.coldab.server.ws;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

  private WebSocketSession session;

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
    System.out.println(message.getPayload());
    session.sendMessage(message);
  }


  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    System.out.println("Connected WebSocket");
    this.session = session;
  }

}
