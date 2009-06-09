package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;
import de.bolay.skat.Card;
import de.bolay.skat.Game;
import de.bolay.skat.Level;
import de.bolay.skat.Position;

import java.util.Set;

public interface BiddingObserver extends Observer {
  public interface Bid {
    void bid(int value);
    void pass();
  }

  public interface Response {
    void accept();
    void pass();
  }

  public interface PickSkat {
    void pickupSkat();
    void announceHandGame(Game game, Level bidLevel);
  }

  public interface AnnounceGame {
    void announceGame(Set<Card> skat, Game game, Level bidLevel);
  }

  void gotCards(Set<Card> cards, Position position,
      String leftOpponent, String rightOpponent);

  void bidSolicited(String listenerName, int nextValue, Bid bid);
  void heardBid(String challengerName, String listenerName, int value);
  void repsonseSolicited(/* Nullable */ String challengerName, int value,
      Response response);
  void heardPass(String announcerName, int value);
  void heardAccept(String announcerName, int value);

  void biddingEnded(/* Nullable */  String soloPlayer);
  void wonBidding(int bidValue, PickSkat pickSkat);
  void gotSkat(Set<Card> skat, AnnounceGame announceGame);
  void skatPickedUp(String soloPlayer);
  void gameAnnounced(String soloPlayer, int bidValue, Game game,
      Level bidLevel);
}
