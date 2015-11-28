package haw.vs.superteam.gamesservice.model;

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


    public String getGameid() {

        return gameid;
    }


    public PlayerCollection getPlayers() {

        return players;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return gameid.equals(game.gameid);
    }

    @Override
    public int hashCode() {
        return gameid.hashCode();
    }
}
