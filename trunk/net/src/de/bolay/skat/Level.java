/**
 *
 */
package de.bolay.skat;

public enum Level {
  REGULAR, HAND, SCHNEIDER_ANNOUNCED, SCHWARZ_ANNOUNCED, OUVERT, HAND_OUVERT;

  public boolean isValid(boolean isNull) {
    switch (this) {
      case REGULAR:
        return true;
      case HAND:
        return true;
      case SCHNEIDER_ANNOUNCED:
        return !isNull;
      case SCHWARZ_ANNOUNCED:
        return !isNull;
      case OUVERT:
        return true;
      case HAND_OUVERT:
        return isNull;
    }
    throw new IllegalStateException("unknown level " + this);
  }

  public void assertValid(Game game) {
    if (!isValid(game.isNull())) {
      throw new IllegalArgumentException(this + "is an invalid bid level for "
          + game);
    }
  }

  public boolean isHand(boolean isNull) {
    return this == HAND || this == SCHNEIDER_ANNOUNCED ||
        this == SCHWARZ_ANNOUNCED || this == (isNull ? HAND_OUVERT : OUVERT);
  }

  public boolean isSchneiderAnnounced(boolean isNull) {
    return !isNull && (this == SCHNEIDER_ANNOUNCED ||
        this == SCHWARZ_ANNOUNCED || this == OUVERT);
  }

  public boolean isSchwarzAnnounced(boolean isNull) {
    return !isNull && (this == SCHWARZ_ANNOUNCED || this == OUVERT);
  }

  public boolean isOuvert() {
    return (this == OUVERT || this == HAND_OUVERT);
  }

  public static Level of(boolean isNull, boolean hand, boolean schneiderAn,
      boolean schwarzAn, boolean ouvert) {

    if (!hand && !schneiderAn && !schwarzAn && !ouvert) {
      return REGULAR;
    } else if (hand && !schneiderAn && !schwarzAn && !ouvert) {
      return HAND;
    }

    if (isNull) {
      if (!hand && !schneiderAn && !schwarzAn && ouvert) {
        return OUVERT;
      } else if (hand && !schneiderAn && !schwarzAn && ouvert) {
        return HAND_OUVERT;
      }
    } else {
      if (hand && schneiderAn && !schwarzAn && !ouvert) {
        return SCHNEIDER_ANNOUNCED;
      } else if (hand && schneiderAn && schwarzAn && !ouvert) {
        return SCHWARZ_ANNOUNCED;
      } else if (hand && schneiderAn && schwarzAn && ouvert) {
        return OUVERT;
      }
    }

    throw new IllegalArgumentException("invalid bid level (null: " + isNull
        + ",hand: " + hand + ", schneiderAn: " + schneiderAn
        + ", schwarzAn: " + schwarzAn + ", ouvert: " + ouvert + ")");
  }
}