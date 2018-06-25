package com.github.coldab.client;

import com.github.coldab.client.gui.ColdabApplication;
import com.github.coldab.shared.ws.MessageEncoder;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javafx.application.Application;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;


public class Main {

  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
  private static String webSocketEndpoint;
  private static String restEndpoint;

  public static void main(String[] args) {
    // Set log level
    Arrays.stream(LogManager.getLogManager().getLogger("")
        .getHandlers()).forEach(h -> h.setLevel(Level.ALL));
    // Parse args
    Options options = new Options();
    options.addOption("debugwebsockets", "Set log level");
    options.addOption("k", "insecure", false, "Use TLS (HTTP and WSS)");
    options.addOption("h", "host", true, "Hosts ip or domain name");
    try {
      CommandLine commandLine = new DefaultParser().parse(options, args);
      if (commandLine.hasOption("debugwebsockets")) {
        MessageEncoder.enableDebugging();
      }
      String host = commandLine.getOptionValue("host");
      if (host == null) {
        host = "coldab.herokuapp.com";
      }
      boolean secure = !commandLine.hasOption("insecure");
      webSocketEndpoint = (secure ? "wss" : "ws") + "://" + host + "/ws/";
      restEndpoint = (secure ? "https" : "http") + "://" + host;
    } catch (Exception exception) {
      LOGGER.severe("Error while parsing CLI arguments:" + exception.getMessage());
      return;
    }

    // Start application
    Application.launch(ColdabApplication.class, args);
  }

  public static String getWebSocketEndpoint() {
    return webSocketEndpoint;
  }

  public static String getRestEndpoint() {
    return restEndpoint;
  }
}
