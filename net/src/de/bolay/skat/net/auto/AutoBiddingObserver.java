package de.bolay.skat.net.auto;

import java.util.Random;
import java.util.Set;

import de.bolay.skat.Card;
import de.bolay.skat.Game;
import de.bolay.skat.Level;
import de.bolay.skat.net.client.observers.BiddingObserver;

public class AutoBiddingObserver implements BiddingObserver {
  Random random = new Random();

  public void bidSolicited(String listenerName, int nextValue, Bid bid) {
    System.out.print("bidSolicited(\"" + listenerName + "\", "
        + nextValue + "): ");
    if (random.nextInt(4) != 0) {
      // 75% chance to bid
      System.out.println("bidding!");
      bid.bid(nextValue);
    } else {
      System.out.println("pass");
      bid.pass();
    }
  }

  public void biddingEnded(String soloPlayer) {
    System.out.println("skatPickedUp(\"" + soloPlayer + "\")");
  }

  public void gameAnnounced(String soloPlayer, int bidValue, Game game,
      Level bidLevel) {
    System.out.println("gameAnnounced(\"" + soloPlayer + "\", "
        + bidValue + ", " + game + ", " + bidLevel + ")");
  }

  public void gotSkat(Set<Card> skat, AnnounceGame announceGame) {
    System.out.println("gotSkat(" + skat + ")");
    announceGame.announceGame(skat, Game.GRAND, Level.REGULAR);
  }

  public void heardAccept(String announcerName, int value) {
    System.out.println("heardAccept(\"" + announcerName + "\", "
        + value + ")");
  }

  public void heardBid(String challengerName, String listenerName,
      int value) {
    System.out.println("heardBid(\"" + challengerName + "\", \""
        + challengerName + "\", " + value + ")");
  }

  public void heardPass(String announcerName, int value) {
    System.out.println("heardPass(\"" + announcerName + "\", "
        + value + ")");
  }

  public void repsonseSolicited(String challengerName, int value,
      Response response) {
    System.out.print("responseSolicited(\"" + challengerName + "\", " + value
        + "): ");
    if (random.nextInt(4) != 0) {
      // 75% chance to accept
      System.out.println("accepting!");
      response.accept();
    } else {
      System.out.println("pass");
      response.pass();
    }
  }

  public void skatPickedUp(String soloPlayer) {
    System.out.println("skatPickedUp(\"" + soloPlayer + "\")");
  }

  public void wonBidding(int bidValue, PickSkat pickSkat) {
    System.out.print("wonBidding(" + bidValue + "): ");
    if (random.nextInt(4) != 0) {
      // 75% chance to pick up skat
      System.out.println("picking up");
      pickSkat.pickupSkat();
    } else {
      System.out.println("playing hand");
      pickSkat.announceHandGame(Game.NULL, Level.HAND);
    }
  }
}