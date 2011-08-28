package de.bolay.skat.net.auto;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import de.bolay.log.Logger;
import de.bolay.skat.net.client.observers.LobbyObserver.Lobby;

public class AutoLobbyObserver {
  private static final String LOBBY_GREETING = "This is a test. I am"
        + " just a program. Do not play with me!";
  protected final Logger log;
  private final int maxPopulation;

  private Set<String> players = new HashSet<String>();
  private int playersJoined;
  private int playersLeft;

  public AutoLobbyObserver(Logger log, int maxPopulation) {
    this.log = log;
    this.maxPopulation = maxPopulation;
  }

  public void entered(Lobby lobby) {
    log.info("entered()");
    lobby.sendChatMessage(LOBBY_GREETING);
  }

  public void chatMessageReceived(String sender, String text) {
    log.info("chatMessageReceived(\"%s\", \"%s\")", sender, text);
  }

  private void assertTablePopulation() {
    int numPlayers = playersJoined - playersLeft;
    assertTrue("negative number of players (joined: " + playersJoined
        + ", left: " + playersLeft + ")", numPlayers >= 0);
    if (maxPopulation > 0) {
      assertTrue("more than " + maxPopulation + " other players (joined: "
          + playersJoined + ", left: " + playersLeft + ")",
          numPlayers < maxPopulation);
    }
  }

  protected void playerJoined(String name) {
    assertTrue("same player (" + name + ") joined lobby twice",
        players.add(name));
    playersJoined++;
    assertTablePopulation();
  }

  public void playerLeft(String name) {
    log.info("playerLeft(\"%s\")", name);
    assertTrue("unknown player (" + name + ") left lobby",
        players.remove(name));
    playersLeft++;
    assertTablePopulation();
  }

  public void serverNotificationReceived(String html) {
    log.info("serverNotificationReceived(\"%s\")", html);
  }
}
