package de.bolay.skat.net.auto;

import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.bolay.log.Logger;
import de.bolay.skat.Card;
import de.bolay.skat.Game;
import de.bolay.skat.Level;
import de.bolay.skat.Position;
import de.bolay.skat.net.client.observers.BiddingObserver;

public class AutoBiddingObserver implements BiddingObserver {
  private final Logger log;
  private final Random random = new Random();
  private final RoundCompletedObserver roundCompletedObserver;

  private Set<Card> cards;

  public AutoBiddingObserver(Logger.Factory logFactory, String playerName,
      @Nullable RoundCompletedObserver roundCompletedObserver) {
    log = logFactory.getLogger(AutoBiddingObserver.class.getName()
        + " for " + playerName);
    this.roundCompletedObserver = roundCompletedObserver;
  }

  public void gotCards(Set<Card> hand, Position position,
      String leftOpponent, String rightOpponent) {
    log.info("gotCards(%s, %s, \"%s\" (playing %s), \"%s\" (playing %s))",
        hand, position,
        leftOpponent, position.before(),
        rightOpponent, position.after());
    cards = Sets.newHashSet(hand); // make mutable copy
  }

  public void bidSolicited(String listenerName, int nextValue, Bid bid) {
    boolean bidding = random.nextInt(4) != 0; // 75% chance to bid
    log.info("bidSolicited(\"%s\", %d): %s", listenerName, nextValue,
        bidding ? "bidding!"  : "pass");
    if (bidding) {
      bid.bid(nextValue);
    } else {
      bid.pass();
    }
  }

  public void biddingEnded(@Nullable String soloPlayer) {
    log.info("biddingEnded(\"%s\")", soloPlayer);
    if (soloPlayer == null) {
      // all passed
      if (roundCompletedObserver != null) {
        roundCompletedObserver.roundCompleted();
      }
    }
  }

  public void gameAnnounced(String soloPlayer, int bidValue, Game game,
      Level bidLevel) {
    log.info("gameAnnounced(\"%s\", %d, %s, %s)",
        soloPlayer, bidValue, game, bidLevel);
  }

  public void gotSkat(Set<Card> skat, AnnounceGame announceGame) {
    log.info("gotSkat(%s)", skat);

    // add skat to cards and discard two random cards
    cards.addAll(skat);
    List<Card> fullHand = Lists.newLinkedList(cards);
    Set<Card> discard = Sets.newHashSet();
    while (discard.size() < 2) {
      Card card = fullHand.remove(random.nextInt(fullHand.size()));
      cards.remove(card);
      discard.add(card);
    }
    log.info("discarding: %s", discard);
    announceGame.announceGame(discard, Game.GRAND, Level.REGULAR);
  }

  public void heardAccept(String announcerName, int value) {
    log.info("heardAccept(\"%s\", %d)", announcerName, value);
  }

  public void heardBid(String challengerName, String listenerName,
      int value) {
    log.info("heardBid(\"%s\", \"%s\", %d)",
        challengerName, listenerName, value);
  }

  public void heardPass(String announcerName, int value) {
    log.info("heardPass(\"%s\", %d)", announcerName, value);
  }

  public void repsonseSolicited(String challengerName, int value,
      Response response) {
    boolean accepting = random.nextInt(4) != 0; // 75% chance to accept
    log.info("responseSolicited(\"%s\", %d): %s", challengerName, value,
        accepting ? "accepting!"  : "pass");
    if (accepting) {
      response.accept();
    } else {
      response.pass();
    }
  }

  public void skatPickedUp(String soloPlayer) {
    log.info("skatPickedUp(\"%s\")", soloPlayer);
  }

  public void wonBidding(int bidValue, PickSkat pickSkat) {
    boolean pickingUp = random.nextInt(4) != 0; // 75% chance to pick up skat
    log.info("wonBidding(%d): ", bidValue,
        pickingUp ? "picking up"  : "playing hand");
    if (pickingUp) {
      pickSkat.pickupSkat();
    } else {
      pickSkat.announceHandGame(Game.NULL, Level.HAND);
    }
  }
}