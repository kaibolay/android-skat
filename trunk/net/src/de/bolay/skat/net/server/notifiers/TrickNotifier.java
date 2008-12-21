package de.bolay.skat.net.server.notifiers;

import de.bolay.pubsub.Notifier;
import de.bolay.pubsub.Observers;
import de.bolay.skat.Card;
import de.bolay.skat.Position;
import de.bolay.skat.net.client.observers.TrickObserver;
import de.bolay.skat.net.client.observers.TrickObserver.Turn;

public class TrickNotifier  {
  private final Observers observers;

  public TrickNotifier(Observers observers) {
    this.observers = observers;
  }

  public void newTrick(final Position position) {
    observers.notify(TrickObserver.class,
        new Notifier<TrickObserver>() {
          public void notify(TrickObserver observer) {
            observer.newTrick(position);
          }
    });
  }

  public void cardPlayed(final String playerName, final Card card) {
    observers.notify(TrickObserver.class,
        new Notifier<TrickObserver>() {
          public void notify(TrickObserver observer) {
            observer.cardPlayed(playerName, card);
          }
    });
  }

  public void cardSolicited(final Turn turn) {
    observers.notify(TrickObserver.class,
        new Notifier<TrickObserver>() {
          public void notify(TrickObserver observer) {
            observer.cardSolicited(turn);
          }
    });
  }

  public void gameOver(final boolean won, final int points, final int score) {
    observers.notify(TrickObserver.class,
        new Notifier<TrickObserver>() {
          public void notify(TrickObserver observer) {
            observer.gameOver(won, points, score);
          }
    });
  }
}
