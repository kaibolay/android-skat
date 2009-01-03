package de.bolay.skat;

import java.util.Comparator;

import com.google.common.base.Predicate;

import de.bolay.skat.Card.Rank;
import de.bolay.skat.Card.Suit;

public enum Game {
  DIAMONDS(Suit.DIAMONDS, false, 9),
  HEARTS(Suit.HEARTS, false, 10),
  SPADES(Suit.SPADES, false, 11),
  CLUBS(Suit.CLUBS, false, 12),
  GRAND(null, false, 24),
  NULL(null, true, 23);

  private final Suit trumpSuit;
  private final boolean isNull;
  private final int value;

  Game(Suit trump, boolean isNull, int value) {
    if (isNull && trump != null) {
      throw new IllegalStateException("There's no trump in null");
    }
    this.trumpSuit = trump;
    this.isNull = isNull;
    this.value = value;
  }

  public Suit getTrumpSuit() {
    return trumpSuit;
  }

  public boolean isNull() {
    return isNull;
  }

  public int getValue() {
    return value;
  }

  public boolean isTrump(Card card) {
    if (isNull) {
      return false;
    }
    return (card.getSuit() == trumpSuit || card.getRank() == Rank.JACK);
  }

  public Comparator<Card> getCardComparator() {
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

  public Predicate<Card> canFollow(final Card leadingCard) {
    return new Predicate<Card>() {
      public boolean apply(Card card) {
        if (isTrump(leadingCard)) {
          return isTrump(card);
        } else {
          return !isTrump(card) && leadingCard.getSuit() == card.getSuit();
        }
      }
    };
  }

  public static Game of(int value) {
    for (Game game : values()) {
      if (game.getValue() == value) {
        return game;
      }
    }
    throw new IllegalArgumentException("No game for value: " + value);
  }

  public boolean trumps(Card card, Card bestCard) {
    return canFollow(bestCard).apply(card) &&
        getCardComparator().compare(card, bestCard) > 0;
  }
}
