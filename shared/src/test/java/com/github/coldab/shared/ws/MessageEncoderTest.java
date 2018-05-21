package com.github.coldab.shared.ws;

import static org.junit.Assert.assertEquals;

import com.github.tjeukayim.socketinterface.SocketMessage;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.google.gson.Gson;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class MessageEncoderTest {

  private SocketReceiver socketReceiver;
  private Receiver receiver;

  @Before
  public void setUp() throws Exception {
    receiver = new Receiver();
    socketReceiver = new SocketReceiver(Protocol.class, receiver);
  }

  @Test
  public void encodeDecode() throws IOException {
    SocketMessage message = new SocketMessage("e", "m", "a1", 2);
    byte[] bytes = MessageEncoder.encodeMessage(message);
    String debug = new String(bytes);
    System.out.println(debug);
    MessageEncoder.decodeMessage(bytes, socketReceiver);
    assertEquals("a1", receiver.a1);
    assertEquals(2, receiver.a2);
  }

  interface Protocol {
    E e();
  }

  interface E {
    void m(String a1, int a2);
  }

  class Receiver implements Protocol {


    private String a1;
    private int a2;

    @Override
    public E e() {
      return (a1, a2) -> {
        this.a1 = a1;
        this.a2 = a2;
      };
    }
  }

  static <T> T encodeDecode(T message) {
    Gson gson = MessageEncoder.getGson();
    String json = gson.toJson(message);
    return (T) gson.fromJson(json, message.getClass());
  }
}