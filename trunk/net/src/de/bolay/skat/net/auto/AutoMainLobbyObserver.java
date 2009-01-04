package de.bolay.skat.net.auto;

import de.bolay.log.Logger;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.MainLobbyObserver;

public class AutoMainLobbyObserver extends AutoLobbyObserver
    implements MainLobbyObserver {
  private static final int UNLIMITED_NUMBER_OF_PLAYERS = -1;

  public AutoMainLobbyObserver(Logger.Factory logFactory, String playerName) {
    super(logFactory.getLogger(AutoMainLobbyObserver.class.getName()
        + "for " + playerName), UNLIMITED_NUMBER_OF_PLAYERS);
  }

  public void playerJoined(String name, Ranking ranking) {
    log.info("playerJoined(\"%s\", %s)", name, ranking);
    super.playerJoined(name);
  }
}