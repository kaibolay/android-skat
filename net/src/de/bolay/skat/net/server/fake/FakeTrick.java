package de.bolay.skat.net.server.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;

import de.bolay.log.Logger;
import de.bolay.pubsub.Observers;
import de.bolay.skat.Card;
import de.bolay.skat.Position;
import de.bolay.skat.net.auto.AutoTrickObserver;
import de.bolay.skat.net.auto.RoundCompletedObserver;
import de.bolay.skat.net.client.observers.TrickObserver.Turn;
import de.bolay.skat.net.server.notifiers.TrickNotifier;

public class FakeTrick {
  private final Logger log;
  private final Map<Position, TrickNotifier> notifiers;
  private final BiddingResult biddingResult;
  private final List<Card> trick = Lists.newArrayListWithExpectedSize(3);

  FakeTrick(Logger.Factory logFactory, Observers observers, String playerName,
      BiddingResult biddingResult) {
    log = logFactory.getLogger(FakeTrick.class.getName());
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
      String name = biddingResult.table.getName(position);
      if (name.equals(playerName)) {
        trickObservers = observers;
      } else {
        trickObservers = new Observers(); // Oma
        trickObservers.add(new AutoTrickObserver(logFactory, name,
            (RoundCompletedObserver) null));
      }
      TrickNotifier notifier = new TrickNotifier(trickObservers);
      notifiers.put(position, notifier);
      notifier.gameStarts(biddingResult.game,
          biddingResult.table.getCards(position));
    }
  }

  Position handle(Position leadingPlayer) {
    log.info("New trick, lead by %s",
        biddingResult.table.getName(leadingPlayer));
    trick.clear();
    Position turn = leadingPlayer;
    Card bestCard = null;
    Position winner = null;

    while (trick.size() < 3) {
      final Set<Card> cards = biddingResult.table.getCards(turn);
      if (cards.size() == 0) {
        handleGameOver();
        return null;
      }
      final String playerName = biddingResult.table.getName(turn);
      notifiers.get(turn).cardSolicited(new Turn() {

        public void playCard(Card card) {
          if (!cards.remove(card)) {
            throw new IllegalArgumentException("Played " + card + " which was"
                + " not in " + cards);
          }
          trick.add(card);
          for (Position position : Position.values()) {
            notifiers.get(position).cardPlayed(playerName, card);
          }
        }
      });
      Card card = trick.get(trick.size()-1); // card just played
      if (trick.size() == 1 || biddingResult.game.trumps(card, bestCard)) {
        log.info("%s played %s, taking over %s", playerName, card, trick);
        bestCard = card;
        winner = turn;
      } else {
        log.info("%s played %s, discarding for %s", playerName, card, trick);
      }
      turn = turn.after();
    }
    log.info("%s won %s", biddingResult.table.getName(winner), trick);
    return winner;
  }

  private void handleGameOver() {
    log.info("game over");
    int points = 0; // TODO: count
    int score = 0; // TODO: determine
    boolean won = false; // TODO: determine

    for (TrickNotifier notifier : notifiers.values()) {
      notifier.gameOver(won, points, score);
    }
  }
}
