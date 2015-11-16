package de.haw_hamburg.vs.wise15.superteam.banks;

import java.util.ArrayList;

/**
 * Created by masha on 16.11.15.
 */

public class Game {
    private String gameId;
    private ArrayList<Player> playerList;
    private Component component;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public Player getPlayerById(String id) {
        for (Player p : playerList) {
            if (p.getPlayerId().equals(id)) {
                return p;
            }
        }
        return null;
    }
}
