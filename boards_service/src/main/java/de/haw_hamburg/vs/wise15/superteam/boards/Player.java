package de.haw_hamburg.vs.wise15.superteam.boards;

/**
 * Created by masha on 16.11.15.
 */

public class Player {

    private String playerId;
    private String name;
    private String uri;
    private Place place;
    private int position;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return playerId.equals(player.playerId);

    }

    @Override
    public int hashCode() {
        return playerId.hashCode();
    }

    public Player(String playerId, String name, String uri, Place place, int position) {
        this.playerId = playerId;
        this.name = name;
        this.uri = uri;
        this.place = place;
        this.position = position;


    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

}
