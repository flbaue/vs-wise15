package de.haw_hamburg.vs.wise15.superteam.banks;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by masha on 16.11.15.
 */

public class Player {

    private Gson gson;
    private String playerId;
    private String name;
    private String uri;
    private Place place;
    private int position;

    public Player(String playerId, String name, String uri, Place place, int position) {
        this.playerId = playerId;
        this.name = name;
        this.uri = uri;
        this.place = place;
        this.position = position;


    }

    public Player(String playerId, String name, String uri, String place, String position) {
        this.playerId = playerId;
        this.name = name;
        this.uri = uri;
        try {
            this.place = gson.fromJson(place, Place.class);
        }catch (JsonSyntaxException e){
            System.out.println("player create: JsonSyntaxException");
        }
        this.position = Integer.parseInt(position);


    }
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



    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

}
