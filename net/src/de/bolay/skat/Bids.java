package de.bolay.skat;

import java.util.SortedSet;
import java.util.TreeSet;

public class Bids {
  private final static SortedSet<Integer> BIDS = new TreeSet<Integer>();

  public static int first() {
    return BIDS.first();
  }

  public static int last() {
    return BIDS.last();
  }

  public static int nextBid(int value) {
    if (value >= last()) {
      throw new IllegalArgumentException("no bid after " + value);
    }
    return BIDS.tailSet(value + 1).first();
  }

  static {
    for (Game game : Game.values()) {
      int value = game.getValue();
      if (game.getTrumpSuit() != null) {
        // Suit-Game
        int maxMultiplier = 17; // 6 Levels + 11 Spitzen (mit/ohne 11, ouvert)
        for (int multiplier = 1; multiplier <= maxMultiplier; multiplier++) {
          BIDS.add(value * ( 1 + multiplier));
        }
      } else {
        if (game.isNull()) {
          // Null-Game
          BIDS.add(value);
          BIDS.add(35); // Hand
          BIDS.add(46); // Ouvert
          BIDS.add(59); // Hand/Ouvert
        } else {
          // Grand-Game
          int maxMultiplier = 10; // 6 levels + 4 Spitzen (mit/ohne 4, ouvert)
          for (int multiplier = 1; multiplier <= maxMultiplier; multiplier++) {
            BIDS.add(value * (1 + multiplier));
          }
        }
      }
    }
  }
}
