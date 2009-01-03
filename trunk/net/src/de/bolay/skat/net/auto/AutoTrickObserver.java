package de.bolay.skat.net.auto;

import static com.google.common.collect.Collections2.filter;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.base.Nullable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.bolay.log.Logger;
import de.bolay.skat.Card;
import de.bolay.skat.Game;
import de.bolay.skat.Position;
import de.bolay.skat.net.client.observers.TrickObserver;

public class AutoTrickObserver implements TrickObserver {
  private final Logger log;
  private final Random random = new Random();
  private final RoundCompletedObserver roundCompletedObserver;
  private final List<Card> trick = Lists.newArrayListWithExpectedSize(3);

  private Game game;
  private Set<Card> cards;

  public AutoTrickObserver(Logger.Factory logFactory, String playerName,
      @Nullable RoundCompletedObserver roundCompletedObserver) {
    log = logFactory.getLogger(AutoTrickObserver.class.getName()
        + " for " + playerName);
    this.roundCompletedObserver = roundCompletedObserver;
  }

  public void gameStarts(Game newGame, Set<Card> newCards) {
    game = newGame;
    cards = Sets.newHashSet(newCards); // make mutable copy
  }

  public void cardPlayed(String playerName, Card card) {
    trick.add(card);
    log.info("cardPlayed(\"%s\", %s), trick: %s", playerName, card, trick);
  }

  public void cardSolicited(Turn yourTurn) {
    List<Card> possibleCards;
    if (trick.size() == 0) {
      possibleCards = Lists.newArrayList(cards);
      log.debug("as the leader any card can be picked: %s", possibleCards);
    } else {
      Card leadingCard = trick.get(0);
      possibleCards = Lists.newArrayList(
          filter(cards, game.canFollow(leadingCard)));
      log.debug("as follower with cards %s, only %s can follow %s",
          cards, possibleCards, leadingCard);
      if (possibleCards.size() == 0) {
        possibleCards = Lists.newArrayList(cards);
        log.debug("since no cards can follow %s, any card can be picked: %s",
            leadingCard, possibleCards);
      }
    }
    Card card = possibleCards.get(random.nextInt(possibleCards.size()));
    log.info("playing %s for trick with %s", card, trick);
    cards.remove(card);
    yourTurn.playCard(card);
  }

  public void gameOver(boolean won, int points, int score) {
    log.info("gameOver(%s, %d, %d)", won, points, score);
    if (roundCompletedObserver != null) {
      roundCompletedObserver.roundCompleted();
    }
  }

  public void newTrick(Position position) {
    log.info("newTrick(%s)", position);
    trick.clear();
  }
}