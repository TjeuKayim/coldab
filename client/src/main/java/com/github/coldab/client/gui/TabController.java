package com.github.coldab.client.gui;

import com.github.coldab.client.project.ProjectController;
import com.github.coldab.client.project.TextFileController;
import com.github.coldab.client.project.TextFileObserver;
import com.github.coldab.shared.project.TextFile;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Tab;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.MultiChangeBuilder;
import org.fxmisc.richtext.model.PlainTextChange;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
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

  private void initializeGui() {
    tab.setText(file.getName());
    tab.setOnClosed(e -> closeTab());

    codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
    tab.setContent(codeArea);

    codeArea
        .multiPlainChanges()
        .successionEnds(Duration.ofMillis(500))
        .subscribe(ignore -> {
          StyleSpans<Collection<String>> styleSpans = computeHighlighting(codeArea.getText());
          if (styleSpans != null) {
            codeArea.setStyleSpans(0, styleSpans);
          }
        });

    eventStream = codeArea.multiPlainChanges().suppressible();
    eventStream.subscribe(this::textChanged);

    codeArea.getStylesheets().add("css/manual-highlighting.css");
  }

  private void closeTab() {
    projectController.closeFile(file);
  }


  private static StyleSpans<Collection<String>> computeHighlighting(String text) {

    Matcher matcher = PATTERN.matcher(text);
    int lastKwEnd = 0;
    StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
    if (!matcher.find()) {
      return null;
    }
    while (matcher.find()) {
      String styleClass =
          matcher.group("KEYWORD") != null ? "keyword" :
              matcher.group("KEYWORD2") != null ? "paren" :
                  matcher.group("BRACE") != null ? "brace" : null;
      assert styleClass != null;
      spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
      spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
      lastKwEnd = matcher.end();
    }
    return spansBuilder.create();
  }

  private static final String[] KEYWORDS = new String[]{
      "DOCTYPE", "html", "head", "title", "/title",
      "head", "/head", "body", "/body", "h1",
      "/h1", "h2", "/h2", "h3", "/h3",
      "p", "/p", "li", "/li", "ul",
      "/ul"
  };
  private static final String[] KEYWORDS2 = new String[]{
      "class", "id"
  };
  private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
  private static final String KEYWORD_PATTERN2 = "\\b(" + String.join("|", KEYWORDS2) + ")\\b";

  private static final String BRACE_PATTERN = "\\<|\\>";
  private static final Pattern PATTERN = Pattern.compile(
      "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
          + "|(?<BRACE>" + BRACE_PATTERN + ")"
          + "|(?<KEYWORD2>" + KEYWORD_PATTERN2 + ")");


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

  @Override
  public void updateText(String text) {
    if (!codeArea.getText().equals(text)) {
      LOGGER.severe("Unresolvable conflict!!!");
      LOGGER.info("Expected: " + text);
      LOGGER.info("Actual: " + codeArea.getText());
      throw new IllegalStateException("Unresolvable conflict");
    }
  }

  @Override
  public void updateAnnotations() {

  }

  @Override
  public void updateTextFile() {

  }

  @Override
  public void remoteEdits(Collection<RemoteDeletion> deletions,
      Collection<RemoteAddition> additions) {
    eventStream.suspendWhile(() -> {
      MultiChangeBuilder<Collection<String>, String, Collection<String>> builder = codeArea
          .createMultiChange();
      deletions.forEach(d -> builder
          .deleteTextAbsolutely(d.getStart() + 1, d.getStart() + d.getLength() + 1));
      additions.forEach(a -> builder
          .insertTextAbsolutely(a.getStart() + 1, a.getText()));
      builder.commit();
    });
  }
}
