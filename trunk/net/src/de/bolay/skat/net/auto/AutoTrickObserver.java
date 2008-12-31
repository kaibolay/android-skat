package de.bolay.skat.net.auto;

import de.bolay.log.Logger;
import de.bolay.skat.Card;
import de.bolay.skat.Position;
import de.bolay.skat.net.client.observers.TrickObserver;

public class AutoTrickObserver implements TrickObserver {
  private final Logger log;

  public AutoTrickObserver(Logger.Factory logFactory) {
    log = logFactory.getLogger(AutoTrickObserver.class.getName());
  }

  public void cardPlayed(String playerName, Card card) {
    log.info("cardPlayed(\"%s\", %s)", playerName, card);
  }

  public void cardSolicited(Turn yourTurn) {
    yourTurn.playCard(null); // TODO
  }

  public void gameOver(boolean won, int points, int score) {
    log.info("gameOver(%s, %d, %d)", won, points, score);
  }

  public void newTrick(Position position) {
    log.info("newTrick(%s)", position);
  }
}