package de.bolay.skat.net.server.fake;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.bolay.log.Logger;
import de.bolay.pubsub.Observers;
import de.bolay.skat.Card;
import de.bolay.skat.Position;
import de.bolay.skat.net.auto.AutoTrickObserver;
import de.bolay.skat.net.client.observers.TrickObserver.Turn;
import de.bolay.skat.net.server.notifiers.TrickNotifier;

public class FakeTrick {
  private final Map<Position, TrickNotifier> notifiers;
  private final Deal deal;
  private final BiddingResult biddingResult;

  FakeTrick(Logger.Factory logFactory, Observers observers, String playerName,
      Deal deal, BiddingResult biddingResult) {
    this.deal = deal;
    this.biddingResult = biddingResult;
    notifiers = new HashMap<Position, TrickNotifier>();

    setupNotifiers(logFactory, observers, playerName);
  }

  /**
   * Set notifiers appropriate for each position (the passed-in for the player,
   * two others for "Omas" which play automatically).
   */
  private void setupNotifiers(Logger.Factory logFactory, Observers observers,
      String playerName) {
    for (Position position : Position.values()) {
      Observers trickObservers;
      if (deal.getName(position).equals(playerName)) {
        trickObservers = observers;
      } else {
        trickObservers = new Observers(); // Oma
        trickObservers.add(new AutoTrickObserver(logFactory));
      }
      notifiers.put(position, new TrickNotifier(trickObservers));
    }
  }

  Position handle(Position player) {
    final Set<Card> playerCards = deal.getCards(player);
    if (playerCards.size() == 0) {
      handleGameOver();
      return null;
    }
    notifiers.get(player).cardSolicited(new Turn() {

      public void playCard(Card card) {
        if (!playerCards.remove(card)) {
          throw new IllegalArgumentException("Played " + card + " which was"
              + " not in " + playerCards);
        }
      }
    });
    return null; // TODO: play trick, find winner
  }

  private void handleGameOver() {
    int points = 0; // TODO: count
    int score = 0; // TODO: determine
    boolean won = false; // TODO: determine

    for (TrickNotifier notifier : notifiers.values()) {
      notifier.gameOver(won, points, score);
    }
  }
}
