package haw.vs.superteam.gamesservice;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import haw.vs.superteam.gamesservice.model.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created by florian on 18.11.15.
 */
public class GameController {

    private Gson gson = new Gson();
    private Set<Game> games;


    public GameCollection getAll() {

        return new GameCollection(new ArrayList<>(games));
    }


    public Optional<Game> createNewGame(String json) {

        Optional<Game> game = null;

        try {
            game = Optional.ofNullable(gson.fromJson(json, Game.class));
        } catch (JsonSyntaxException e) {
            game = Optional.empty();
        }

        if (game.isPresent()) {
            games.add(game.get());
        }

        return game;
    }


    public Optional<Game> getGame(String gameId) {

        Optional<Game> game = games.stream()
                .filter(g -> g.getGameid().equals(gameId))
                .findAny();

        return game;
    }


    public Optional<PlayerCollection> getPlayersFromGame(String gameId) {

        Optional<Game> game = games.stream()
                .filter(g -> g.getGameid().equals(gameId))
                .findAny();

        Optional<PlayerCollection> players;
        if (game.isPresent()) {
            players = Optional.ofNullable(game.get().getPlayers());
        } else {
            players = Optional.empty();
        }
        return Optional.empty();
    }


    public Optional<Player> getPlayerFromGame(String gameId, String playerId) {

        return games.stream()
                .filter(g -> g.getGameid().equals(gameId))
                .map(Game::getPlayers)
                .filter(Objects::nonNull)
                .flatMap(pc -> pc.getPlayers().stream())
                .filter(p -> p.getId().equals(playerId))
                .findAny();
    }


    public Boolean addPlayerToGame(String gameId, String playerId, String playerName, String playerURI) {
        new Player();



        return null;
    }


    public void removePlayerFromGame(String gameId, String playerId) {


    }


    public boolean isPlayerReady(String gameId, String playerId) {

        return false;
    }


    public void togglePlayerReady(String gameId, String playerId) {

    }

    public Player getCurrentPlayer(String gameId) {
        return null;
    }

    public Player getPlayWithMutex(String gameId) {
        return null;
    }

    public MutexStatus setMutexToPlayer(String gameId, String playerId) {
        return null;
    }

    public void removeMutex(String gameId) {

    }
}
