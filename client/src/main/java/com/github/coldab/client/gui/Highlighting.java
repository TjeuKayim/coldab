package com.github.coldab.client.gui;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

public class Highlighting {

  static StyleSpans<Collection<String>> compute(String text) {
    /**
     * Gets Text
     * @matcher Matches Tekst to matching keywords or patterns
     * @spansBuilder Adds styles to patterns
     */

    Matcher matcher = PATTERN.matcher(text);
    int lastKwEnd = 0;
    StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
    int counter = 0;
    while (matcher.find()) {
      String styleClass =
          matcher.group("KEYWORD") != null ? "keyword" :
              matcher.group("KEYWORD2") != null ? "paren" :
                  matcher.group("COMMENT") != null ? "comment" :
                      matcher.group("BRACE") != null ? "brace" : "default";
      assert styleClass != null;
      spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
      spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
      lastKwEnd = matcher.end();
      counter++;
    }
    if (counter == 0) {

      return null;
    }
    return spansBuilder.create();
  }


  private static final String[] KEYWORDS = new String[]{
      /**
       * Adds keywords used for matching
       * Edit this to add or remove keywords to be styled
      */
      "DOCTYPE", "html", "head", "title", "/title",
      "head", "/head", "body", "/body", "h1",
      "/h1", "h2", "/h2", "h3", "/h3",
      "p", "/p", "li", "/li", "ul",
      "/ul"
  };
  private static final String[] KEYWORDS2 = new String[]{
      /**
       * Enables Keywords to differ in Style from others
      */

      "class", "id"
  };
  private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";

  private static final String KEYWORD_PATTERN2 = "\\b(" + String.join("|", KEYWORDS2) + ")\\b";
  private static final String BRACE_PATTERN = "\\<|\\>";

  private static final String COMMENT_PATTERN = "<!--(?<=\\<!--)(.*?)(?=\\-->)-->";

  private static final Pattern PATTERN = Pattern.compile(
      "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
          + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
          + "|(?<BRACE>" + BRACE_PATTERN + ")"
          + "|(?<KEYWORD2>" + KEYWORD_PATTERN2 + ")");
}
