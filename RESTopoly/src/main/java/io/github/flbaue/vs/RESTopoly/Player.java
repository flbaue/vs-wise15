package io.github.flbaue.vs.RESTopoly;

/**
 * Created by florian on 24.10.15.
 */
public class Player {

    private String playerId;
    private int roundsInJail;
    private boolean isInJail;

    public Player(String playerId, int roundsInJail) {
        this.playerId = playerId;
        this.roundsInJail = roundsInJail;
    }

    public Player(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getRoundsInJail() {
        return roundsInJail;
    }

    public boolean isInJail() {
        return isInJail;
    }

    public void addRound() {
        roundsInJail++;
    }

    public void setIsInJail(boolean isInJail) {
        this.isInJail = isInJail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return !(playerId != null ? !playerId.equals(player.playerId) : player.playerId != null);

    }

    @Override
    public int hashCode() {
        return playerId != null ? playerId.hashCode() : 0;
    }
}
