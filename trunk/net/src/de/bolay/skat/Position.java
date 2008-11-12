package de.bolay.skat;

public enum Position {
  FORE_HAND, MIDDLE_HAND, BACK_HAND;

  public static Position before(Position position) {
    switch (position) {
      case FORE_HAND: return BACK_HAND;
      case MIDDLE_HAND: return FORE_HAND;
      case BACK_HAND: return MIDDLE_HAND;
    }
    throw new IllegalStateException("Unknown position: " + position);
  }

  public static Position after(Position position) {
    switch (position) {
      case FORE_HAND: return MIDDLE_HAND;
      case MIDDLE_HAND: return BACK_HAND;
      case BACK_HAND: return FORE_HAND;
    }
    throw new IllegalStateException("Unknown position: " + position);
  }
}