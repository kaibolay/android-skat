/**
 *
 */
package de.bolay.skat.net.auto;

import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.MainLobbyObserver;

public class AutoMainLobbyObserver implements MainLobbyObserver {
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