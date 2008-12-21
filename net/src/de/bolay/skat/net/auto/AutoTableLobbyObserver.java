package de.bolay.skat.net.auto;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import de.bolay.skat.Card;
import de.bolay.skat.Position;
import de.bolay.skat.net.client.observers.TableLobbyObserver;

public class AutoTableLobbyObserver implements TableLobbyObserver {
  private static final String TABLE_LOBBY_GREETING = "This is a test. I am"
      + " just a program. Do not play with me!";
  Set<String> players = new HashSet<String>();
  private int playersJoined;
  private int playersLeft;
  private int serverNotificationsReceived;

  public void entered(TableLobby tableLobby) {
    System.out.println("TableLobbyObserver.entered()");
    tableLobby.sendChatMessage(TABLE_LOBBY_GREETING);
  }

  public void chatMessageReceived(String sender, String text) {
    System.out.println("TableLobbyObserver.chatMessageReceived(\"" + sender
        + "\", \"" + text + "\")");
  }

  private void assertTablePopulation() {
    int numPlayers = playersJoined - playersLeft;
    assertTrue("negative number of players (joined: " + playersJoined
        + ", left: " + playersLeft + ")", numPlayers >= 0);
    assertTrue("more than two other players (joined: " + playersJoined
        + ", left: " + playersLeft + ")", numPlayers < 3);
  }

  public void playerJoined(String name) {
    System.out.println("TableLobbyObserver.playerJoined(\"" + name + "\")");
    assertTrue("same player (" + name + ") joined table twice",
        players.add(name));
    playersJoined++;
    assertTablePopulation();
  }

  public void playerLeft(String name) {
    System.out.println("TableLobbyObserver.playerLeft(\"" + name + "\")");
    assertTrue("unknown player (" + name + ") left table",
        players.remove(name));
    playersLeft++;
    assertTablePopulation();
  }

  public void serverNotificationReceived(String html) {
    System.out.println(
        "MainLobbyObserver.serverNotificationReceived(\"" + html + "\")");
    serverNotificationsReceived++;
  }

  public void gotCards(Set<Card> hand, Position position,
      String leftOpponent, String rightOpponent) {
    System.out.println("gotCards(" + hand + ", " + position + ", \""
        + leftOpponent + "\" (playing " + position.before() + "), \""
        + rightOpponent + "\" (playing " + position.after() + "))");
  }
}