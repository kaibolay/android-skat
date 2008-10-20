package de.bolay.skat.net.client;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import de.bolay.skat.Card;
import de.bolay.skat.Game.Position;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.ChallengingBiddingObserver;
import de.bolay.skat.net.client.observers.MainLobbyObserver;
import de.bolay.skat.net.client.observers.PassiveBiddingObserver;
import de.bolay.skat.net.client.observers.PendingLoginObserver;
import de.bolay.skat.net.client.observers.RespondingBiddingObserver;
import de.bolay.skat.net.client.observers.TableLobbyObserver;

public class AutoPlayer {
  private static final long WAIT = 3 * 1000;
  private static final long TIMEOUT = 60 * 1000;
  
  private final ServerConnection connection;
  
  public AutoPlayer(ServerConnection connection, 
      String username, String password) {
    this.connection = connection;

    connection.addObserver(new SimplePendingLoginObserver(
        username, password));
    connection.addObserver(new SimpleMainLobbyObserver());
    connection.addObserver(new SimpleTableLobbyObserver());
    connection.addObserver(new SimplePassiveBiddingObserver());
    connection.addObserver(new SimpleChallengingBiddingObserver());
    connection.addObserver(new SimpleRespondingBiddingObserver());
  }
  
  public void play() {
    connection.open();
    for (int i = 0; i < TIMEOUT/WAIT; i++) {
      if (!connection.isUp()) {
        fail("Connection died");
      }
      System.out.println("live connection in iteration #" + i + " sleeping...");
      try {
        Thread.sleep(WAIT);
      } catch (InterruptedException ie) {
        fail(ie.getMessage());
      }
    }
  }
  
  private static class SimplePendingLoginObserver 
      implements PendingLoginObserver {
    
    private final String username;
    private final String password;

    SimplePendingLoginObserver (String username, String password) {
      this.username = username;
      this.password = password;
    }

    public void pendingLogin(PendingLogin pendingLogin) {
      System.out.println("PendingLoginObserver.pendingLogin()");
      pendingLogin.attemptLogin(username, password);
    }

    public void loginFailed(LoginStatus status, PendingLogin pendingLogin) {
      fail("login failed: " + status);
    }

    public void loginSucceeded(Ranking ranking) {
      System.out.println("PendingLoginObserver.loginSucceeded(" 
          + ranking + ")");
    }

    public void serverBusy() {
      fail("Server busy");
    }
  }

  private static class SimpleMainLobbyObserver implements MainLobbyObserver {
    private static final String MAIN_LOBBY_GREETING = "This is a test. I am"
        + " just a program. Do not play with me!";

    public void entered(MainLobby mainLobby) {
      System.out.println("MainLobbyObserver.entered()");
      mainLobby.sendChatMessage(MAIN_LOBBY_GREETING);
    }

    public void serverNotificationReceived(String html) {
      System.out.println(
          "MainLobbyObserver.serverNotificationReceived(\"" + html + "\")");
    }

    public void playerJoined(String name, Ranking ranking) {
      System.out.println("MainLobbyObserver.playerJoined(" + name + ", "
          + ranking + ")");
    }

    public void playerLeft(String name) {
      System.out.println("MainLobbyObserver.playerLeft(" + name + ")");
    }

    public void chatMessageReceived(String sender, String text) {
      System.out.println("MainLobbyObserver.chatMessageReceived(\"" + sender
          + "\", \"" + text + "\")");
    }
  }

  private static class SimpleTableLobbyObserver implements TableLobbyObserver {
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

    public void gotCards(Set<Card> hand, Position position) {
      System.out.println("gotCards(" + hand + ", " + position + ")");
    }
  }

  private static class SimplePassiveBiddingObserver 
      implements PassiveBiddingObserver {
    public void hearBid(String playerName, int value) {
      System.out.println("hearBid(\"" + playerName + "\", " + value + ")");
    }

    public void hearResponse(String playerName, boolean accepted) {
      System.out.println("hearResponse(\"" + playerName + "\", "
          + accepted + ")");
    }
  }

  private static class SimpleChallengingBiddingObserver 
      implements ChallengingBiddingObserver {
    Random random = new Random();

    public void bidSolicited(String playerName, int nextValue, Bid bid) {
      System.out.print("bidSolicited (\"" + playerName + "\", >= "
          + nextValue + "): ");
      if (random.nextInt(4) == 0) {
        // 25% chance to bid
        System.out.println("bidding!");
        bid.bid(nextValue);
      } else {
        System.out.println("pass");
        bid.pass();
      }
    }
  }
  
  private static class SimpleRespondingBiddingObserver 
      implements RespondingBiddingObserver {
    Random random = new Random();

    public void offerReceived(String playerName, int value, Response response) {
      System.out.print("offerReceived(\"" + playerName + "\", " + value
          + "): ");
      if (random.nextInt(4) == 0) {
        // 25% chance to accept
        System.out.println("accepting!");
        response.accept();
      } else {
        System.out.println("pass");
        response.pass();
      }
    }
  }
}
