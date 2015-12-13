package de.haw_hamburg.vs.wise15.superteam.banks;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

/**
 * Created by masha on 16.11.15.
 */

public class Game {
    private Gson gson;
    private String gameId;
    private ArrayList<Player> playerList;
    private Component component;

    public Game(String s, ArrayList<String> players, Component component) {
        this.gameId=s;
        Player pl;
        for(String el:players){
            try {
                pl = gson.fromJson(el, Player.class);
                this.playerList.add(pl);
            }catch (JsonSyntaxException e){

            }

        }
        this.component=component;
    }

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
