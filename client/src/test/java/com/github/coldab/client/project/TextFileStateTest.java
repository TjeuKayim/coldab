package com.github.coldab.client.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.github.coldab.client.project.TextFileState.Observer;
import com.github.coldab.shared.edit.Addition;
import com.github.coldab.shared.project.TextFile;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;

public class TextFileStateTest implements Observer {

  private TextFile file;
  private TextFileState state;
  private String text = "";

  @Before
  public void setUp() throws Exception {
    file = new TextFile("/test.txt");
    state = new TextFileState(file);
    state.addObserver(this);
  }

  @Test
  public void remoteChange() {
    Addition addition = new Addition(0, null, null, "Hello World");
    state.addRemoteEdit(addition);
    assertEquals("Hello World", text);
    assertEquals(file.getEdits(), Collections.singletonList(addition));
  }

  @Test(expected = IllegalStateException.class)
  public void invalidRemoteIndex() {
    Addition alpha = new Addition(0, null, null, "Hello World");
    Addition beta = new Addition(0, null, null, "Hello World");
    state.addRemoteEdit(alpha);
    state.addRemoteEdit(beta);
  }

  @Test
  public void localChange() {
    text = "Local change";
    Addition addition = new Addition(null, null, "Local change");
    state.addLocalEdit(addition);
    assertEquals(file.getEdits(), Collections.emptyList());
  }

  @Test
  public void confirmation() {
    text = "Local change";
    Addition alpha = new Addition(null, null, "Local change\n");
    Addition beta = new Addition(null, null, "Yet to be confirmed");
    state.addLocalEdit(alpha);
    state.addLocalEdit(beta);
    // Confirm first change
    Addition confirmedAlpha = new Addition(0, null, null, "Local change\n");
    state.confirmLocalEdit(confirmedAlpha);
    assertEquals(file.getEdits(), Collections.singletonList(confirmedAlpha));
  }

  @Test(expected = IllegalStateException.class)
  public void invalidConfirmationIndex() {
    text = "Local change";
    Addition alpha = new Addition(null, null, "Local change\n");
    state.addLocalEdit(alpha);
    state.confirmLocalEdit(new Addition(2, null, null, "invalid"));
  }

  @Override
  public void updateText(String text) {
    assertNotEquals("New text should be differnt", text, this.text);
    this.text = text;
  }
}