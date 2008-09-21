package de.bolay.skat.net;

/** Standing of a player on the score board */
public class Ranking {
  private final int played;
  private final int won;
  private final int lost;
  private final int opponents;
  private final int points;

  public Ranking(int played, int won, int lost, int opponents, int points) {
    this.played = played;
    this.won = won;
    this.lost = lost;
    this.opponents = opponents;
    this.points = points;
  }

  public int getPlayed() {
    return played;
  }

  public int getWon() {
    return won;
  }

  public int getLost() {
    return lost;
  }

  public int getOpponents() {
    return opponents;
  }

  public int getPoints() {
    return points;
  }

  @Override
  public String toString() {
    return "played: " + played + ", won: " + won + ", lost: " + lost
        + ", opponents: " + opponents + ", points: " + points;
  }
}