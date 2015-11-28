package haw.vs.superteam.gamesservice.model;

import java.util.List;

/**
 * Created by florian on 16.11.15.
 */
public class PlayerCollection {

    private List<Player> players;


    public PlayerCollection() {

    }


    public PlayerCollection(List<Player> players) {

        this.players = players;
    }


    public List<Player> getPlayers() {

        return players;
    }
}
