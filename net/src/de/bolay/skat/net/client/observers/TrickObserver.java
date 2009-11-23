package de.bolay.skat.net.client.observers;

import java.util.Set;

import de.bolay.pubsub.Observer;
import de.bolay.skat.Card;
import de.bolay.skat.Game;
import de.bolay.skat.Level;
import de.bolay.skat.Position;

public interface TrickObserver extends Observer {
  public interface Turn {
    void playCard(Card card);
  }

  void gameStarts(Game announcedGame, Level announcedLevel, Set<Card> cards);

  void newTrick(Position position);
  void cardPlayed(String playerName, Card card);
  void cardSolicited(Turn yourTurn);

  void gameOver(boolean won, int point, int score);
}
