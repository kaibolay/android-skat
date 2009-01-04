package de.bolay.skat.net.auto;

import de.bolay.log.Logger;
import de.bolay.skat.net.client.observers.TableLobbyObserver;

public class AutoTableLobbyObserver extends AutoLobbyObserver
    implements TableLobbyObserver {
  private static final int MAX_PLAYERS_PER_TABLE = 3;

  public AutoTableLobbyObserver(Logger.Factory logFactory, String playerName) {
    super(logFactory.getLogger(AutoTableLobbyObserver.class.getName()
        + "for " + playerName), MAX_PLAYERS_PER_TABLE);
  }

  @Override
  public void playerJoined(String name) {
    log.info("playerJoined(\"%s\")", name);
    super.playerJoined(name);
  }
}