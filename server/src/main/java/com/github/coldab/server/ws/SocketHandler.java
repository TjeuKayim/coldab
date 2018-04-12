package com.github.coldab.server.ws;

import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.MessageEncoder;
import com.github.coldab.shared.ws.ServerEndpoint;
import com.github.tjeukayim.socketinterface.SocketMessage;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.github.tjeukayim.socketinterface.SocketSender;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

  private WebSocketSession session;
  private SocketReceiver socketReceiver;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    // TODO: Connect endpoints
    System.out.println("Connected WebSocket");
    this.session = session;
    ServerEndpoint serverEndpoint = SocketSender.create(ServerEndpoint.class, this::sendMessage);
    ClientEndpoint clientEndpoint = null;
    socketReceiver = new SocketReceiver(ClientEndpoint.class, clientEndpoint);
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    byte[] bytes = message.asBytes();
    MessageEncoder.decodeMessage(bytes, socketReceiver);
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
