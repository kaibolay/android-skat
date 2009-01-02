package de.bolay.skat.net.server.fake;

import static de.bolay.skat.Position.BACK_HAND;
import static de.bolay.skat.Position.FORE_HAND;
import static de.bolay.skat.Position.MIDDLE_HAND;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Nullable;
import com.google.common.collect.ImmutableSet;

import de.bolay.log.Logger;
import de.bolay.pubsub.Observers;
import de.bolay.skat.Bids;
import de.bolay.skat.Card;
import de.bolay.skat.Game;
import de.bolay.skat.Level;
import de.bolay.skat.Position;
import de.bolay.skat.net.auto.AutoBiddingObserver;
import de.bolay.skat.net.client.observers.BiddingObserver;
import de.bolay.skat.net.client.observers.BiddingObserver.AnnounceGame;
import de.bolay.skat.net.client.observers.BiddingObserver.Bid;
import de.bolay.skat.net.client.observers.BiddingObserver.PickSkat;
import de.bolay.skat.net.server.notifiers.BiddingNotifier;

class FakeBidding {
  private final Map<Position, BiddingNotifier> notifiers;
  private final Deal deal;
  private final Set<Position> stillBidding;
  private final Bids bids;

  public FakeBidding(Logger.Factory logFactory, Observers observers,
      String playerName, Deal deal) {
    this.deal = deal;
    notifiers = new HashMap<Position, BiddingNotifier>();
    stillBidding = new HashSet<Position>(Arrays.asList(Position.values()));
    bids = new Bids();

    setupNotifiers(logFactory, observers, playerName);
  }

  /**
   * Set notifiers appropriate for each position (the passed-in for the player,
   * two others for "Omas" which bid automatically).
   */
  private void setupNotifiers(Logger.Factory logFactory, Observers observers,
      String playerName) {
    for (Position position : Position.values()) {
      Observers positionObservers;
      boolean isPlayer = deal.getName(position).equals(playerName);
      if (isPlayer) {
        positionObservers = observers;
      } else {
        positionObservers = new Observers(); // Oma
        positionObservers.add(new AutoBiddingObserver(logFactory));
      }
      BiddingNotifier notifier = new BiddingNotifier(positionObservers);
      notifiers.put(position, notifier);
      if (isPlayer) {
        notifier.gotCards(position,
            ImmutableSet.copyOf(deal.getCards(playerName)),
            deal.getName(position.after()), deal.getName(position.before()));
      }
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
      // both bidders passed, does FORE_HAND want to play?
      solicitResponse(FORE_HAND, /* challenger */ (String) null, finalBid);
    }
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
        BiddingNotifier notifier = notifiers.get(position);
        if (accept) {
          notifier.announceAccept(name, bid);
        } else {
          notifier.announcePass(name, bid);
        }
      }
    }
  }

  private void solicitResponse(final Position listener,
      @Nullable String challengerName, final int currentBid) {

    final String listenerName = deal.getName(listener);
    notifiers.get(listener).solicitResponse(challengerName,
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
    notifiers.get(challenger).solicitBid(
        deal.getName(listener), currentBid, new Bid() {

          public void bid(int value) {
            notifiers.get(observer).announceBid(
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
            notifiers.get(observer).announcePass(challengerName, currentBid);
            stillBidding.remove(challenger);
          }
        });
  }

  private void handleAllPassed() {
    for (Position position : Position.values()) {
      notifiers.get(position).biddingEnded(/* soloPlayer */ (String) null);
    }
  }

  private BiddingResult handleAnnouncement(final int finalBid) {
    final BiddingResult result = new BiddingResult();
    result.soloPosition = stillBidding.iterator().next(); // only element
    String soloName = deal.getName(result.soloPosition);
    for (Position position : Position.values()) {
      if (position != result.soloPosition) {
        notifiers.get(position).biddingEnded(soloName);
      }
    }
    final BiddingNotifier soloNotifier = notifiers.get(result.soloPosition);
    soloNotifier.wonBidding(finalBid, new PickSkat() {

      public void announceHandGame(Game game, Level level) {
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
        soloNotifier.gotSkat(ImmutableSet.copyOf(deal.getSkat()),
            new AnnounceGame() {

              public void announceGame(Set<Card> skat, Game game, Level level) {
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