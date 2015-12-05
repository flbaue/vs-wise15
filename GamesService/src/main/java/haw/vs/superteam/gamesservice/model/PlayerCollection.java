package haw.vs.superteam.gamesservice.model;

import java.util.Iterator;
import java.util.LinkedList;
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

    public void addPlayer(Player player) {
        if (players == null) {
            players = new LinkedList<>();
        }
        players.add(player);
        int position = players.indexOf(player);
        player.setPosition(position);
    }

    public void removePlayer(String playerId) {
        if (players == null) {
            return;
        }
        Player player;
        Iterator<Player> iterator = players.iterator();
        while (iterator.hasNext()) {
            player = iterator.next();
            if (player.getId().equals(playerId)) {
                iterator.remove();
            }
        }
        return;
    }
}
