package com.github.coldab.client.gui;

import com.github.coldab.client.project.ProjectComponent;
import com.github.coldab.client.project.TextFileController;
import com.github.coldab.client.project.TextFileObserver;
import com.github.coldab.shared.project.TextFile;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.PlainTextChange;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.SuspendableEventStream;

public class TabController implements TextFileObserver {


  private final TextFile file;
  private final Tab tab;
  private final TextFileController textFileController;
  private final CodeArea codeArea = new CodeArea();
  private SuspendableEventStream<List<PlainTextChange>> eventStream;

  public TabController(TextFile file, Tab tab, ProjectComponent projectComponent) {
    this.file = file;
    this.tab = tab;
    initializeGUI();
    this.textFileController = projectComponent.openFile(file, this);
  }

  private void initializeGUI() {
    tab.setText(file.getName());

    codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
    tab.setContent(codeArea);

    /*
    codeArea
        .multiPlainChanges()
        .successionEnds(Duration.ofMillis(500))
        .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
    */

    eventStream = codeArea.multiPlainChanges().suppressible();
    eventStream.subscribe(this::textChanged);

    codeArea.getStylesheets().add("css/manual-highlighting.css");
  }


  private static StyleSpans<Collection<String>> computeHighlighting(String text) {

    Matcher matcher = PATTERN.matcher(text);
    int lastKwEnd = 0;
    StyleSpansBuilder<Collection<String>> spansBuilder
        = new StyleSpansBuilder<>();
    while (matcher.find()) {
      String styleClass =
          matcher.group("KEYWORD") != null ? "keyword" :
              matcher.group("KEYWORD2") != null ? "paren" :

                  matcher.group("BRACE") != null ? "brace" :

                      null;
      assert styleClass != null;
      spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
      spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
      lastKwEnd = matcher.end();
    }

    //System.out.println(text);
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
    Platform.runLater(() -> eventStream.suspendWhile(() -> codeArea.replaceText(text)));
  }

  @Override
  public void updateAnnotations() {

  }

  @Override
  public void updateTextFile() {

  }
}
