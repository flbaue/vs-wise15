package haw.vs.superteam.gamesservice.model;

import java.util.*;

/**
 * Created by florian on 16.11.15.
 */
public class PlayerCollection {

    private List<Player> players;

    public List<Player> getPlayers() {
        if (players == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(players);
        }
    }

    public synchronized boolean addPlayer(Player player) {
        if (players == null) {
            players = new ArrayList<>();
        }

        if (containsPlayer(player)) {
            return false;
        } else {
            players.add(player);
            int position = players.indexOf(player);
            player.setPosition(position);
            return true;
        }
    }

    public synchronized void removePlayer(String playerId) {
        if (players != null && !players.isEmpty()) {
            Player player;
            Iterator<Player> iterator = players.iterator();
            while (iterator.hasNext()) {
                player = iterator.next();
                if (player.getId().equals(playerId)) {
                    iterator.remove();
                }
            }
        }
    }

    public boolean containsPlayer(Player player) {
        if (players == null || players.isEmpty()) {
            return false;
        } else {
            return players.stream().filter(p -> p.equals(player)).findAny().isPresent();
        }
    }

    public Player getPlayer(String playerId) {
        Optional<Player> player = getPlayers().stream().filter(p -> p.getId().equals(playerId)).findAny();
        return player.orElse(null);
    }

    public boolean allPlayersReady(){
        return !getPlayers().stream().filter(p -> !p.isReady()).findAny().isPresent();
    }

    public boolean isEmpty(){
        return getPlayers().isEmpty();
    }
}
