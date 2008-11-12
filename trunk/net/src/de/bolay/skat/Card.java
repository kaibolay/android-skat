package de.bolay.skat;

import java.util.EnumSet;
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
    SEVEN(0), EIGHT(0), NINE(0), TEN(10), JACK(2), QUEEN(3), KING(4), ACE(11);

    private final int value;

    Rank(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
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
    Set<Card> ofSuit = EnumSet.noneOf(Card.class);
    for (Card card : values()) {
      if (card.getSuit() == suit) {
        ofSuit.add(card);
      }
    }
    return ofSuit;
  }

  public static Set<Card> ofRank(Rank rank) {
    Set<Card> ofRank = EnumSet.noneOf(Card.class);
    for (Card card : Card.values()) {
      if (card.getRank() == rank) {
        ofRank.add(card);
      }
    }
    return ofRank;
  }
}
