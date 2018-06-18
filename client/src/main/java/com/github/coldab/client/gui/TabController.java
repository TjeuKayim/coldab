package com.github.coldab.client.gui;

import com.github.coldab.client.project.ProjectController;
import com.github.coldab.client.project.TextFileController;
import com.github.coldab.client.project.TextFileObserver;
import com.github.coldab.shared.project.TextFile;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
   *this methode gets called if there are remote changes made to the file.
   */
  @Override
  public void remoteEdits(Collection<RemoteDeletion> deletions,
      Collection<RemoteAddition> additions) {
    int caret = codeArea.getCaretPosition();
    int posistion = 0;
    AtomicInteger deleted = new AtomicInteger();
    AtomicInteger largedeleted = new AtomicInteger();
    AtomicInteger added = new AtomicInteger(-2);
    AtomicInteger largeadded = new AtomicInteger();

    eventStream.suspendWhile(() -> {
      MultiChangeBuilder<Collection<String>, String, Collection<String>> builder = codeArea
          .createMultiChange();
      for (RemoteDeletion d : deletions) {
        builder.deleteTextAbsolutely(d.getStart() + 1, d.getStart() + d.getLength() + 1);
        deleted.set(d.getStart());
        largedeleted.set(d.getLength());
      }
      for (RemoteAddition a : additions) {
        builder.insertTextAbsolutely(a.getStart() + 1, a.getText());
        added.set(a.getStart());
        largeadded.set(a.getText().length());
      }
      builder.commit();
    });

    if (deleted.get() != 0) {
      if (deleted.get() <= caret) {
        posistion = 0;

        if (largedeleted.get() > 1) {
          posistion = -largedeleted.get();
        } else {
          caret = caret - 1;
        }

      }
    }
    if (added.get() != -2) {
      if (added.get() <= caret) {
        if (largeadded.get() > 1) {
          posistion = largeadded.get();
        } else {
          posistion = 1;
        }
      }
    }
    if(caret + posistion <= codeArea.getLength())
    {codeArea.moveTo(caret + posistion);}
    else {codeArea.moveTo(codeArea.getLength());}
  }
}
