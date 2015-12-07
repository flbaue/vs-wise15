package haw.vs.superteam.gamesservice.model;

import java.util.Objects;

/**
 * Created by florian on 16.11.15.
 */
public class Game {

    private String gameid;
    private PlayerCollection players;
    private boolean started;
    private String uri;
    private Components components;
    private transient Player currentPlayer;
    private transient Player mutexPlayer;


    public Game() {
    }


    public Game(String gameid, PlayerCollection players, boolean started, String uri, Components components) {
        Objects.requireNonNull(gameid);

        this.gameid = gameid;
        this.players = players;
        this.started = started;
        this.uri = uri;
        this.components = components;
    }

    public Game(String gameid) {
        this.gameid = gameid;
        this.players = new PlayerCollection();
    }


    public String getGameid() {

        return gameid;
    }

    public void setGameid(String gameid) {
        Objects.requireNonNull(gameid);
        this.gameid = gameid;
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

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Components getComponents() {

        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public boolean addNewPlayer(Player player) {
        if (players == null) {
            players = new PlayerCollection();
        }
        return players.addPlayer(player);
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

    public void removePlayer(String playerId) {
        players.removePlayer(playerId);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getMutexPlayer() {
        return mutexPlayer;
    }

    public void setMutexPlayer(Player mutexPlayer) {
        this.mutexPlayer = mutexPlayer;
    }

    public Player getPlayer(String playerId) {
        if (players == null) {
            return null;
        } else {
            return players.getPlayer(playerId);
        }
    }
}
