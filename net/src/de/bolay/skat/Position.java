package de.bolay.skat;

public enum Position {
  FORE_HAND, MIDDLE_HAND, BACK_HAND;

  public Position before() {
    switch (this) {
      case FORE_HAND: return BACK_HAND;
      case MIDDLE_HAND: return FORE_HAND;
      case BACK_HAND: return MIDDLE_HAND;
    }
    throw new IllegalStateException("Unknown position: " + this);
  }

  public Position after() {
    switch (this) {
      case FORE_HAND: return MIDDLE_HAND;
      case MIDDLE_HAND: return BACK_HAND;
      case BACK_HAND: return FORE_HAND;
    }
    throw new IllegalStateException("Unknown position: " + this);
  }
}