package de.bolay.skat.net.server.fake;

import static de.bolay.skat.Position.BACK_HAND;
import static de.bolay.skat.Position.FORE_HAND;
import static de.bolay.skat.Position.MIDDLE_HAND;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import de.bolay.log.Logger;
import de.bolay.skat.Bids;
import de.bolay.skat.Card;
import de.bolay.skat.Game;
import de.bolay.skat.Level;
import de.bolay.skat.Position;
import de.bolay.skat.net.auto.AutoBiddingObserver;
import de.bolay.skat.net.auto.RoundCompletedObserver;
import de.bolay.skat.net.client.observers.BiddingObserver;
import de.bolay.skat.net.client.observers.BiddingObserver.AnnounceGame;
import de.bolay.skat.net.client.observers.BiddingObserver.Bid;
import de.bolay.skat.net.client.observers.BiddingObserver.PickSkat;

class FakeBidding {
  private final Logger log;
  private final Map<Position, BiddingObserver> observers;
  private final Deal deal;
  private final Set<Position> stillBidding;
  private final Bids bids;

  public FakeBidding(Logger.Factory logFactory, BiddingObserver biddingObserver,
      String playerName, Deal deal) {
    log = logFactory.getLogger(FakeBidding.class.getName());
    this.deal = deal;
    observers = new HashMap<Position, BiddingObserver>();
    stillBidding = new HashSet<Position>(Arrays.asList(Position.values()));
    bids = new Bids();

    setupObservers(logFactory, biddingObserver, playerName);
  }

  /**
   * Set observers appropriate for each position (the passed-in for the player,
   * two others for "Omas" which bid automatically).
   */
  private void setupObservers(Logger.Factory logFactory,
      BiddingObserver playerBiddingObserver, String playerName) {
    for (Position position : Position.values()) {
      BiddingObserver biddingObserver;
      String name = deal.getName(position);
      if (name.equals(playerName)) {
        biddingObserver = playerBiddingObserver;
      } else {
        biddingObserver = new AutoBiddingObserver(logFactory, name,
            (RoundCompletedObserver) null);
      }
      observers.put(position, biddingObserver);
      biddingObserver.gotCards(ImmutableSet.copyOf(deal.getCards(position)),
          position, deal.getName(position.after()),
          deal.getName(position.before()));
    }
  }

  BiddingResult handle() {
    final int finalBid = handleBids();
    if (stillBidding.size()  == 0) {
      handleAllPassed();
      return null;
    } else if (stillBidding.size()  == 1) {
      final BiddingResult result = handleAnnouncement(finalBid);
      return result;
    } else {
      throw new IllegalStateException("Bidding should be over, but still"
          + " bidding are: " + stillBidding);
    }
  }

  private int handleBids() {
    handleMiddleHandBidding();
    handleBackHandBidding();
    int finalBid = bids.current();
    if (finalBid == Bids.first() && stillBidding.contains(FORE_HAND)) {
      log.info("both bidders passed, does FORE_HAND want to play?");
      solicitResponse(FORE_HAND, /* challenger */ (String) null, finalBid);
    }
    log.info("final bid %d with %s still bidding", finalBid, stillBidding);
    return finalBid;
  }

  private void handleMiddleHandBidding() {
    while (stillBidding.contains(MIDDLE_HAND)) {
      if (stillBidding.contains(FORE_HAND)) {
        doBid(MIDDLE_HAND, FORE_HAND, BACK_HAND);
      } else if (stillBidding.contains(BACK_HAND)) {
        doBid(BACK_HAND, MIDDLE_HAND, FORE_HAND);
      } else {
        break;
      }
    }
  }

  private void handleBackHandBidding() {
    while (stillBidding.contains(BACK_HAND)) {
      if (stillBidding.contains(FORE_HAND)) {
        doBid(BACK_HAND, FORE_HAND, MIDDLE_HAND);
      } else {
        break;
      }
    }
  }

  private void announceResponse(String name, int bid, boolean accept) {
    for (Position position : Position.values()) {
      if (!name.equals(deal.getName(position))) {
        BiddingObserver observer = observers.get(position);
        if (accept) {
          observer.heardAccept(name, bid);
        } else {
          observer.heardPass(name, bid);
        }
      }
    }
  }

  private void solicitResponse(final Position listener,
      @Nullable String challengerName, final int currentBid) {

    final String listenerName = deal.getName(listener);
    observers.get(listener).repsonseSolicited(challengerName,
        currentBid, new BiddingObserver.Response() {

          public void accept() {
            announceResponse(listenerName, currentBid, true);
          }

          public void pass() {
            announceResponse(listenerName, currentBid, false);
            stillBidding.remove(listener);
          }
    });
  }

  private void doBid(final Position challenger, final Position listener,
      final Position observer) {
    final String challengerName = deal.getName(challenger);
    final String listenerName = deal.getName(listener);
    final int currentBid = bids.current();
    log.info("%s (%s) is challenging %s (%s) for at least %d",
        challengerName, challenger, listenerName, listener, currentBid);
    observers.get(challenger).bidSolicited(
        deal.getName(listener), currentBid, new Bid() {

          public void bid(int value) {
            observers.get(observer).heardBid(
                challengerName, listenerName, currentBid);
            if (bids.isLast()) {
              // maxed out, listener plays!
              stillBidding.remove(challenger);
              stillBidding.remove(observer);
            } else {
              solicitResponse(listener, challengerName, currentBid);
              bids.next();
            }
          }

          public void pass() {
            observers.get(observer).heardPass(challengerName, currentBid);
            stillBidding.remove(challenger);
          }
        });
  }

  private void handleAllPassed() {
    log.info("all passed");
    for (Position position : Position.values()) {
      observers.get(position).biddingEnded(/* soloPlayer */ (String) null);
    }
  }

  private BiddingResult handleAnnouncement(final int finalBid) {
    final BiddingResult result = new BiddingResult();
    result.table = deal.getTable();
    result.soloPosition = stillBidding.iterator().next(); // only element
    String soloName = deal.getName(result.soloPosition);

    log.info("bidding ended at %d with %s (%s) playing solo",
        finalBid, soloName, result.soloPosition);
    for (Position position : Position.values()) {
      if (position != result.soloPosition) {
        observers.get(position).biddingEnded(soloName);
      }
    }
    final BiddingObserver soloObserver = observers.get(result.soloPosition);
    soloObserver.wonBidding(finalBid, new PickSkat() {

      public void announceHandGame(Game game, Level level) {
        log.info("announceHandGame(%s, %s)", game, level);
        if (!level.isHand(game.isNull())) {
          throw new IllegalArgumentException("Not picking up skat, but not"
              + " announcing hand with " + level + " for " + game);
        }
        result.game = game;
        result.level = level;
        result.skat = deal.getSkat();
      }

      public void pickupSkat() {
        // modify hand...
        final Set<Card> cards = deal.getCards(result.soloPosition);
        cards.addAll(deal.getSkat());
        log.info("picket up skat ", deal.getSkat());
        soloObserver.gotSkat(ImmutableSet.copyOf(deal.getSkat()),
            new AnnounceGame() {

              public void announceGame(Set<Card> skat, Game game, Level level) {
                log.info("announceGame(%s, %s, %s)", skat, game, level);
                if (level.isHand(game.isNull())) {
                  throw new IllegalArgumentException("Picked up skat, but"
                      + " announcing hand with " + level + " for " + game);
                }
                cards.removeAll(skat);
                if (skat.size() != 2 || cards.size() != 10) {
                  throw new IllegalArgumentException("Weird skat return: "
                      + skat + " - resulted in cards: " + cards);
                }
                result.game = game;
                result.level = level;
                result.skat = skat;
              }
          });
      }
    });
    return result;
  }
}