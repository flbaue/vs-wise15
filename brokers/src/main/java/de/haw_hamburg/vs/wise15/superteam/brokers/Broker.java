package de.haw_hamburg.vs.wise15.superteam.brokers;

import java.util.ArrayList;

/**
 * Created by masha on 28.11.15.
 */
public class Broker {

    //brokerId == gameId
    private String brokerId;
    private Game game;
   // private Map<Place, Estate> soldPlaces = new HashMap<>();
    private ArrayList<Place> notSold = new ArrayList<Place>();

    public Broker(String brokerId, Game game) {
        this.brokerId = brokerId;
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ArrayList<Place> getNotSold() {
        return notSold;
    }

    public void setNotSold(ArrayList<Place> notSold) {
        this.notSold = notSold;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }
}
