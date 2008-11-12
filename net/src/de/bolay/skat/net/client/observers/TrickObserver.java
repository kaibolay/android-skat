package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;
import de.bolay.skat.Card;
import de.bolay.skat.Position;

public interface TrickObserver extends Observer {
  public interface Turn {
    void playCard(Card card);
  }

  void newTrick(Position position);
  void cardPlayed(String playerName, Card card);
  void cardSolicited(Turn yourTurn);

  void gameOver(boolean won, int point, int score);
}
