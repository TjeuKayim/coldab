package com.github.coldab.client.ws;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebsocketClientEndpoint {

  public static void main(String[] args) throws URISyntaxException, InterruptedException {
    new WebsocketClientEndpoint(new URI("ws://localhost:8080/ws"));
    Thread.sleep(1000);
  }

  Session session = null;

  public WebsocketClientEndpoint(URI endpointURI) {
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      container.connectToServer(this, endpointURI);
    } catch (DeploymentException | IOException e) {
      // TODO: handle errors
      e.printStackTrace();
    }
  }

  @OnOpen
  public void onOpen(Session session) throws IOException {
    this.session = session;
    session.getBasicRemote().sendText("Hello World");
  }

  @OnClose
  public void onClose(Session session, CloseReason reason) {
    this.session = null;
  }

  @OnMessage
  public void onMessage(String message) {
    // TODO: call message receiver
    System.out.println(message);
  }

  @OnError
  public void onWebSocketError(Throwable cause) {

  }
}
