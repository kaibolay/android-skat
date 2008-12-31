package de.bolay.skat.net.auto;

import de.bolay.log.Logger;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.MainLobbyObserver;

public class AutoMainLobbyObserver implements MainLobbyObserver {
  private static final String MAIN_LOBBY_GREETING = "This is a test. I am"
      + " just a program. Do not play with me!";

  private final Logger log;

  public AutoMainLobbyObserver(Logger.Factory logFactory) {
    log = logFactory.getLogger(AutoMainLobbyObserver.class.getName());
  }

  public void entered(MainLobby mainLobby) {
    log.info("MainLobbyObserver.entered()");
    mainLobby.sendChatMessage(MAIN_LOBBY_GREETING);
  }

  public void serverNotificationReceived(String html) {
    log.info("MainLobbyObserver.serverNotificationReceived(\"%s\")", html);
  }

  public void playerJoined(String name, Ranking ranking) {
    log.info("MainLobbyObserver.playerJoined(\"%s\", %s)", name, ranking);
  }

  public void playerLeft(String name) {
    log.info("MainLobbyObserver.playerLeft(\"%s\", %s)", name);
  }

  public void chatMessageReceived(String sender, String text) {
    log.info("MainLobbyObserver.chatMessageReceived(\"%s\", \"%s\")",
        sender, text);
  }
}