package com.github.coldab.client.gui;

import com.github.coldab.client.project.ProjectController;
import com.github.coldab.client.project.TextFileController;
import com.github.coldab.client.project.TextFileObserver;
import com.github.coldab.shared.project.TextFile;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javafx.scene.control.Tab;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.MultiChangeBuilder;
import org.fxmisc.richtext.model.PlainTextChange;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.SuspendableEventStream;

public class TabController implements TextFileObserver {


  private final TextFile file;
  private final Tab tab;
  private final TextFileController textFileController;
  private final CodeArea codeArea = new CodeArea();
  private final ProjectController projectController;
  private SuspendableEventStream<List<PlainTextChange>> eventStream;
  private static final Logger LOGGER = Logger.getLogger(TabController.class.getName());


  public TabController(TextFile file, Tab tab, ProjectController projectController) {
    this.file = file;
    this.tab = tab;
    initializeGui();
    this.projectController = projectController;
    this.textFileController = projectController.openFile(file, this);
  }

  /**
   * update the gui if a file is deleted.
   */
  public void fileDeleted() {
    tab.getTabPane().getTabs().remove(tab);
  }

  private void initializeGui() {
    tab.setText(file.getName());
    tab.setOnClosed(e -> closeTab());

    codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
    tab.setContent(codeArea);

    codeArea
        .multiPlainChanges()
        .successionEnds(Duration.ofMillis(500))
        .subscribe(ignore -> {
          StyleSpans<Collection<String>> styleSpans = Highlighting.compute(codeArea.getText());
          if (styleSpans != null) {
            codeArea.setStyleSpans(0, styleSpans);
          }
        });

    eventStream = codeArea.multiPlainChanges().suppressible();
    eventStream.subscribe(this::textChanged);

    codeArea.getStylesheets().add("css/manual-highlighting.css");
  }

  /**
   * close a file tab in the editor, and update this in the gui.
   */
  private void closeTab() {
    projectController.closeFile(file);
  }

  /**
   * this methode gets called if the are changes made to the file.
   *
   * @param changes list of changes that are made to the file
   */
  private void textChanged(List<PlainTextChange> changes) {
    for (PlainTextChange change : changes) {
      String inserted = change.getInserted();
      String removed = change.getRemoved();
      if (!inserted.equals("")) {
        int position = change.getPosition();
        textFileController.createAddition(position - 1, inserted);
      }
      if (!removed.equals("")) {
        int position = change.getPosition();
        int length = change.getNetLength();
        textFileController.createDeletion(position - 1, -length);
      }
    }
  }

  /**
   * update the text of the file inside the gui
   */
  @Override
  public void updateText(String text) {
    if (!codeArea.getText().equals(text)) {
      LOGGER.severe("Unresolvable conflict!!!");
      LOGGER.info(() -> "Expected: " + text);
      LOGGER.info("Actual: " + codeArea.getText());
      throw new IllegalStateException("Unresolvable conflict");
    }
  }

  /**
   * Called if there are remote changes made to the file.
   */
  @Override
  public void remoteEdits(Collection<RemoteDeletion> deletions,
      Collection<RemoteAddition> additions) {
    eventStream.suspendWhile(() -> {
      int caret = codeArea.getCaretPosition();
      int position = 0;
      MultiChangeBuilder<Collection<String>, String, Collection<String>> builder = codeArea
          .createMultiChange();
      for (RemoteDeletion d : deletions) {
        builder.deleteTextAbsolutely(d.getStart() + 1, d.getStart() + d.getLength() + 1);
        int index = d.getStart();
        int length = d.getLength();
        if (index != 0 && index <= caret) {
          position -= length;
        }
      }
      for (RemoteAddition a : additions) {
        builder.insertTextAbsolutely(a.getStart() + 1, a.getText());
        int index = a.getStart();
        int length = a.getText().length();
        if (index != -2 && index <= caret) {
          position += length;
        }
      }
      builder.commit();
      caret += position;
      if (caret <= codeArea.getLength()) {
        caret = codeArea.getLength();
      }
      codeArea.moveTo(caret);
    });
  }
}
