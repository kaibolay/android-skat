package de.bolay.skat;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public enum Card {
  SEVEN_OF_DIAMONDS(Rank.SEVEN, Suit.DIAMONDS),
  EIGHT_OF_DIAMONDS(Rank.EIGHT, Suit.DIAMONDS),
  NINE_OF_DIAMONDS(Rank.NINE, Suit.DIAMONDS),
  QUEEN_OF_DIAMONDS(Rank.QUEEN, Suit.DIAMONDS),
  KING_OF_DIAMONDS(Rank.KING, Suit.DIAMONDS),
  TEN_OF_DIAMONDS(Rank.TEN, Suit.DIAMONDS),
  ACE_OF_DIAMONDS(Rank.ACE, Suit.DIAMONDS),
  SEVEN_OF_HEARTS(Rank.SEVEN, Suit.HEARTS),
  EIGHT_OF_HEARTS(Rank.EIGHT, Suit.HEARTS),
  NINE_OF_HEARTS(Rank.NINE, Suit.HEARTS),
  QUEEN_OF_HEARTS(Rank.QUEEN, Suit.HEARTS),
  KING_OF_HEARTS(Rank.KING, Suit.HEARTS),
  TEN_OF_HEARTS(Rank.TEN, Suit.HEARTS),
  ACE_OF_HEARTS(Rank.ACE, Suit.HEARTS),
  SEVEN_OF_SPADES(Rank.SEVEN, Suit.SPADES),
  EIGHT_OF_SPADES(Rank.EIGHT, Suit.SPADES),
  NINE_OF_SPADES(Rank.NINE, Suit.SPADES),
  QUEEN_OF_SPADES(Rank.QUEEN, Suit.SPADES),
  KING_OF_SPADES(Rank.KING, Suit.SPADES),
  TEN_OF_SPADES(Rank.TEN, Suit.SPADES),
  ACE_OF_SPADES(Rank.ACE, Suit.SPADES),
  SEVEN_OF_CLUBS(Rank.SEVEN, Suit.CLUBS),
  EIGHT_OF_CLUBS(Rank.EIGHT, Suit.CLUBS),
  NINE_OF_CLUBS(Rank.NINE, Suit.CLUBS),
  QUEEN_OF_CLUBS(Rank.QUEEN, Suit.CLUBS),
  KING_OF_CLUBS(Rank.KING, Suit.CLUBS),
  TEN_OF_CLUBS(Rank.TEN, Suit.CLUBS),
  ACE_OF_CLUBS(Rank.ACE, Suit.CLUBS),
  JACK_OF_DIAMONDS(Rank.JACK, Suit.DIAMONDS),
  JACK_OF_HEARTS(Rank.JACK, Suit.HEARTS),
  JACK_OF_SPADES(Rank.JACK, Suit.SPADES),
  JACK_OF_CLUBS(Rank.JACK, Suit.CLUBS);

  public static final int NUM_CARDS = Card.values().length;

  public enum Rank {
    SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
  }

  public static final int NUM_RANKS = Rank.values().length;

  public enum Suit {
    DIAMONDS, HEARTS, SPADES, CLUBS
  }

  public static final int NUM_SUITS = Suit.values().length;

  private final Suit suit;
  private final Rank rank;

  Card(Rank rank, Suit suit) {
    this.rank = rank;
    this.suit = suit;
  }

  public Suit getSuit() {
    return suit;
  }

  public Rank getRank() {
    return rank;
  }

  public static Set<Card> ofSuit(Suit suit) {
    Set<Card> ofSuit = new HashSet<Card>(NUM_RANKS);
    for (Card card : values()) {
      if (card.getSuit() == suit) {
        ofSuit.add(card);
      }
    }
    return ofSuit;
  }

  public static Set<Card> ofRank(Rank rank) {
    Set<Card> ofRank = new HashSet<Card>(NUM_SUITS);
    for (Card card : Card.values()) {
      if (card.getRank() == rank) {
        ofRank.add(card);
      }
    }
    return ofRank;
  }

  public static Comparator<Card> getSuitGameComparator(final Suit trump) {
    return new Comparator<Card>() {
        public int compare(Card c1, Card c2) {
          if (c1.rank == Rank.JACK) {
            if (c2.rank == Rank.JACK) {
              return c1.suit.compareTo(c2.suit);
            } else {
              return 1; // c1 trumps
            }
          }
          if (c1.suit == trump) {
            if (c2.rank == Rank.JACK) {
              return -1; // c2 trumps
            }
            if (c2.suit == trump) {
              return c1.compareTo(c2);
            } else {
              return 1; // c1 trumps
            }
          }
          if (c2.rank == Rank.JACK || c2.suit == trump) {
            return -1; // c2 trumps
          }
          return c1.compareTo(c2);
        }
    };
  }

  public static Comparator<Card> getGrandGameComparator() {
    return getSuitGameComparator(null);
  }

  public static Comparator<Card> getNullGameComparator() {
    return new Comparator<Card>() {
        public int compare(Card c1, Card c2) {
          int r = c1.suit.compareTo(c2.suit);
          if (r != 0) {
            return r;
          }
          return c1.rank.compareTo(c2.rank);
        }
    };
  }
}
