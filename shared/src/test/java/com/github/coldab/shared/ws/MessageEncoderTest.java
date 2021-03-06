package com.github.coldab.shared.ws;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.github.tjeukayim.socketinterface.SocketMessage;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import java.io.IOException;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;

public class MessageEncoderTest {

  private SocketReceiver socketReceiver;
  private Receiver receiver;

  @Before
  public void setUp() {
    receiver = new Receiver();
    socketReceiver = new SocketReceiver(Protocol.class, receiver);
  }

  @Test
  public void encodeDecode() throws IOException {
    MyObject[] myObjects = new MyObject[] {new MyObject("hello"), new MyObject("world")};
    SocketMessage message =
        new SocketMessage("e", "m", myObjects, 2);
    byte[] bytes = MessageEncoder.encodeMessage(message);
    String debug = new String(bytes);
    System.out.println(debug);
    MessageEncoder.decodeMessage(bytes, socketReceiver);
    assertArrayEquals(myObjects, receiver.a1);
    assertEquals(2, receiver.a2);
  }

  interface Protocol {
    E e();
  }

  interface E {
    void m(MyObject[] a1, int a2);
  }

  static class Receiver implements Protocol {

    private MyObject[] a1;
    private int a2;

    @Override
    public E e() {
      return (a1, a2) -> {
        this.a1 = a1;
        this.a2 = a2;
      };
    }
  }

  static class MyObject {
    @Expose
    final String text;

    MyObject(String text) {
      this.text = text;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof MyObject)) {
        return false;
      }
      MyObject myObject = (MyObject) o;
      return Objects.equals(text, myObject.text);
    }
  }

  static <T> T encodeDecode(T message) {
    Gson gson = MessageEncoder.getGson();
    String json = gson.toJson(message);
    System.out.println(json);
    return (T) gson.fromJson(json, message.getClass());
  }
}