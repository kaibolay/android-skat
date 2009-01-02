package de.bolay.skat.net.auto;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import de.bolay.log.Logger;
import de.bolay.skat.net.client.observers.TableLobbyObserver;

public class AutoTableLobbyObserver implements TableLobbyObserver {
  private static final String TABLE_LOBBY_GREETING = "This is a test. I am"
      + " just a program. Do not play with me!";

  private final Logger log;

  private Set<String> players = new HashSet<String>();
  private int playersJoined;
  private int playersLeft;
  private int serverNotificationsReceived;

  public AutoTableLobbyObserver(Logger.Factory logFactory) {
    log = logFactory.getLogger(AutoTableLobbyObserver.class.getName());
  }

  public void entered(TableLobby tableLobby) {
    log.info("TableLobbyObserver.entered()");
    tableLobby.sendChatMessage(TABLE_LOBBY_GREETING);
  }

  public void chatMessageReceived(String sender, String text) {
    log.info("TableLobbyObserver.chatMessageReceived(\"%s\", \"%s\")",
        sender, text);
  }

  private void assertTablePopulation() {
    int numPlayers = playersJoined - playersLeft;
    assertTrue("negative number of players (joined: " + playersJoined
        + ", left: " + playersLeft + ")", numPlayers >= 0);
    assertTrue("more than two other players (joined: " + playersJoined
        + ", left: " + playersLeft + ")", numPlayers < 3);
  }

  public void playerJoined(String name) {
    log.info("TableLobbyObserver.playerJoined(\"%s\")", name);
    assertTrue("same player (" + name + ") joined table twice",
        players.add(name));
    playersJoined++;
    assertTablePopulation();
  }

  public void playerLeft(String name) {
    log.info("TableLobbyObserver.playerLeft(\"%s\")", name);
    assertTrue("unknown player (" + name + ") left table",
        players.remove(name));
    playersLeft++;
    assertTablePopulation();
  }

  public void serverNotificationReceived(String html) {
    log.info("MainLobbyObserver.serverNotificationReceived(\"%s\")", html);
    serverNotificationsReceived++;
  }
}