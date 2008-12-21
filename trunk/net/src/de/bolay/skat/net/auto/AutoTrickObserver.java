/**
 *
 */
package de.bolay.skat.net.auto;

import de.bolay.skat.Card;
import de.bolay.skat.Position;
import de.bolay.skat.net.client.observers.TrickObserver;

public class AutoTrickObserver implements TrickObserver {

  public void cardPlayed(String playerName, Card card) {
    System.out.println("cardPlayed(\"" + playerName + "\", " + card + ")");
  }

  public void cardSolicited(Turn yourTurn) {
    yourTurn.playCard(null); // TODO
  }

  public void gameOver(boolean won, int points, int score) {
    System.out.println("gameOver(" + won + ", " + points + ", "
        + score + ")");
  }

  public void newTrick(Position position) {
    System.out.println("newTrick(" + position + ")");
  }
}