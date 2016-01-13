package de.hawhamburg.vs.wise15.superteam.client.model;

/**
 * Created by florian on 16.11.15.
 */
public class Game {

    private String gameid;
    private PlayerCollection players;
    private boolean started;
    private String uri;
    private Components components;


    public Game() {

    }


    public Game(String gameid, PlayerCollection players, boolean started, String uri, Components components) {

        this.gameid = gameid;
        this.players = players;
        this.started = started;
        this.uri = uri;
        this.components = components;
    }

    public Game(Components components) {
        this.components = components;
    }


    public String getGameid() {

        return gameid;
    }


    public PlayerCollection getPlayers() {

        return players;
    }

    public void setPlayers(PlayerCollection players) {
        this.players = players;
    }

    public boolean isStarted() {

        return started;
    }

    public String getUri() {

        return uri;
    }

    public Components getComponents() {

        return components;
    }

    @Override
    public String toString() {

        return gameid;
    }
}
