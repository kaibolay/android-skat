package de.bolay.skat.net.server.notifiers;

import java.util.Set;

import com.google.common.base.Nullable;

import de.bolay.pubsub.Notifier;
import de.bolay.pubsub.Observers;
import de.bolay.skat.Card;
import de.bolay.skat.Game;
import de.bolay.skat.Level;
import de.bolay.skat.net.client.observers.BiddingObserver;
import de.bolay.skat.net.client.observers.BiddingObserver.AnnounceGame;
import de.bolay.skat.net.client.observers.BiddingObserver.Bid;
import de.bolay.skat.net.client.observers.BiddingObserver.PickSkat;
import de.bolay.skat.net.client.observers.BiddingObserver.Response;

public class BiddingNotifier  {
  private final Observers observers;

  public BiddingNotifier(Observers observers) {
    this.observers = observers;
  }

  public void solicitBid(final String listenerName, final int nextValue,
      final Bid bid) {
    observers.notify(BiddingObserver.class,
        new Notifier<BiddingObserver>() {
          public void notify(BiddingObserver observer) {
            observer.bidSolicited(listenerName, nextValue, bid);
          }
    });
  }

  public void announceBid(final String challengerName,
      final String listenerName, final int value) {
    observers.notify(BiddingObserver.class,
        new Notifier<BiddingObserver>() {
          public void notify(BiddingObserver observer) {
            observer.heardBid(challengerName, listenerName, value);
          }
    });
  }

  public void solicitResponse(@Nullable final String challengerName,
      final int value, final Response response) {
    observers.notify(BiddingObserver.class,
        new Notifier<BiddingObserver>() {
          public void notify(BiddingObserver observer) {
            observer.repsonseSolicited(challengerName, value, response);
          }
    });
  }

  public void announcePass(final String announcerName, final int value) {
    observers.notify(BiddingObserver.class,
        new Notifier<BiddingObserver>() {
          public void notify(BiddingObserver observer) {
            observer.heardPass(announcerName, value);
          }
    });
  }

  public void announceAccept(final String announcerName, final int value) {
    observers.notify(BiddingObserver.class,
        new Notifier<BiddingObserver>() {
          public void notify(BiddingObserver observer) {
            observer.heardAccept(announcerName, value);
          }
    });
  }

  public void wonBidding(final int bidValue, final PickSkat pickSkat) {
    observers.notify(BiddingObserver.class,
        new Notifier<BiddingObserver>() {
          public void notify(BiddingObserver observer) {
            observer.wonBidding(bidValue, pickSkat);
          }
    });
  }

  public void biddingEnded(final String soloPlayer) {
    observers.notify(BiddingObserver.class,
        new Notifier<BiddingObserver>() {
          public void notify(BiddingObserver observer) {
            observer.biddingEnded(soloPlayer);
          }
    });
  }

  public void gotSkat(final Set<Card> skat, final AnnounceGame announceGame) {
    observers.notify(BiddingObserver.class,
        new Notifier<BiddingObserver>() {
          public void notify(BiddingObserver observer) {
            observer.gotSkat(skat, announceGame);
          }
    });
  }

  public void skatPickedUp(final String soloPlayer) {
    observers.notify(BiddingObserver.class,
        new Notifier<BiddingObserver>() {
          public void notify(BiddingObserver observer) {
            observer.skatPickedUp(soloPlayer);
          }
    });
  }

  public void gameAnnounced(final String soloPlayer, final int bidValue,
      final Game game, final Level bidLevel) {
    observers.notify(BiddingObserver.class,
        new Notifier<BiddingObserver>() {
          public void notify(BiddingObserver observer) {
            observer.gameAnnounced(soloPlayer, bidValue, game, bidLevel);
          }
    });
  }
}
