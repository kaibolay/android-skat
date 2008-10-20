package de.bolay.skat;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import de.bolay.skat.Card.Rank;
import de.bolay.skat.Card.Suit;

public enum Game {
  DIAMONDS(Suit.DIAMONDS, false, 9), 
  HEARTS(Suit.HEARTS, false, 10), 
  SPADES(Suit.SPADES, false, 11), 
  CLUBS(Suit.CLUBS, false, 12),
  NULL(null, true, 23),
  GRAND(null, false, 24); 
  
  public enum Position {
    FORE_HAND, MIDDLE_HAND, BACK_HAND
  }
  
  private final Suit trumpSuit;
  private final boolean isNull;
  private final int value;
  public final static SortedSet<Integer> BIDS = new TreeSet<Integer>();
  
  static {
    for (int gameValue = 9; gameValue <= 12; gameValue++) {
      for (int multiplier = 2; multiplier <= 22; multiplier++) {
        BIDS.add(gameValue * multiplier);
      }
    }
    BIDS.add(23); // Null
    BIDS.add(35); // Null-Hand
    BIDS.add(46); // Null-Ouvert
    BIDS.add(59); // Null-Ouvert-Hand
  }
  
  Game(Suit trump, boolean isNull, int value) {
    if (isNull && trump != null) {
      throw new IllegalStateException("There's no trump in null");
    }
    this.trumpSuit = trump;
    this.isNull = isNull;
    this.value = value;
  }
  
  Suit getTrumpSuit() {
    return trumpSuit;
  }
  
  int getValue() {
    return value;
  }
  
  boolean isTrump(Card card) {
    if (isNull) {
      return false;
    }
    return (card.getSuit() == trumpSuit || card.getRank() == Rank.JACK);
  }
  
  public Comparator<Card> getCardComperator() {
    if (isNull) {
      return new Comparator<Card>() {
          public int compare(Card c1, Card c2) {
            int r = c1.getSuit().compareTo(c2.getSuit());
            if (r != 0) {
              return r;
            }
            return c1.getRank().compareTo(c2.getRank());
          }
      };
    } else {
      return new Comparator<Card>() {
          public int compare(Card c1, Card c2) {
            if (c1.getRank() == Rank.JACK) {
              if (c2.getRank() == Rank.JACK) {
                return c1.getSuit().compareTo(c2.getSuit());
              } else {
                return 1; // c1 trumps
              }
            }
            if (c1.getSuit() == trumpSuit) {
              if (c2.getRank() == Rank.JACK) {
                return -1; // c2 trumps
              }
              if (c2.getSuit() == trumpSuit) {
                return c1.compareTo(c2);
              } else {
                return 1; // c1 trumps
              }
            }
            if (c2.getRank() == Rank.JACK || c2.getSuit() == trumpSuit) {
              return -1; // c2 trumps
            }
            return c1.compareTo(c2);
          }
      };
    }
  }
}
