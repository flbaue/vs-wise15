package haw.vs.superteam.gamesservice;

import haw.vs.superteam.gamesservice.api.BoardsAPI;
import haw.vs.superteam.gamesservice.model.*;
import retrofit.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * Created by florian on 18.11.15.
 */
public class GameController {

    private static Logger logger = Utils.getLogger(GameController.class.getName());

    private static AtomicLong gameCounter = new AtomicLong(0);
    private Set<Game> games = new HashSet<>();
    private Components components;
    private BoardsAPI boardsAPI;

    public GameController(Components components, BoardsAPI boardsAPI) throws IOException {
        this.components = components;
        this.boardsAPI = boardsAPI;
    }

    public GameCollection getAll() {

        return new GameCollection(new ArrayList<>(games));
    }


    public Game createNewGame() {

        Game game = new Game(String.valueOf(gameCounter.incrementAndGet()));
        games.add(game);

        game.setComponents(components);
        game.setUri(components.getGame() + "/games/" + game.getGameid());

        try {
            Response<Board> response = boardsAPI.createBoard(game.getGameid()).execute();
            if (response.isSuccess()) {
                game.getComponents().setBoard(components.getBoard());
            }
        } catch (IOException e) {
            games.remove(game);
            return null;
        }

        return game;
    }


    public Game getGame(String gameId) {
        for (Game game : games) {
            if (game.getGameid().equals(gameId)) {
                return game;
            }
        }
        return null;
    }


    public Boolean addPlayerToGame(String gameId, String playerId, String playerName, String playerURI) {
        Game game = getGame(gameId);
        if (game == null) {
            return false;
        }

        Player player = new Player(playerId, playerName, playerURI);
        boolean playerAdded = game.addNewPlayer(player);
        if (playerAdded) {
            try {
                Response<Void> response = boardsAPI.addPlayer(game.getGameid(), player.getId(), player).execute();
                if (response.isSuccess()) {
                    logger.info("Player " + playerId + " added to board");
                } else {
                    logger.severe(response.toString());
                    playerAdded = false;
                }
            } catch (IOException e) {
                logger.severe(e.getMessage());
                playerAdded = false;
            }
        }

        return playerAdded;
    }


    public Player getPlayerFromGame(String gameId, String playerId) {
        Game game = getGame(gameId);
        if (game == null) {
            return null;
        }

        return game.getPlayer(playerId);
    }

    public void togglePlayerReady(String gameId, String playerId) {
        Player player = getPlayerFromGame(gameId, playerId);
        if (player != null) {
            player.setReady(!player.isReady());
        }
    }

    public void removePlayerFromGame(String gameId, String playerId) {
        Game game = getGame(gameId);
        if (game != null) {
            game.removePlayer(playerId);
        }
    }

    public Boolean isPlayerReady(String gameId, String playerId) {
        Player player = getPlayerFromGame(gameId, playerId);
        if (player != null) {
            return player.isReady();
        }
        return false;
    }

    public PlayerCollection getPlayersFromGame(String gameId) {
        Game game = getGame(gameId);
        if (game == null) {
            return new PlayerCollection();
        }
        return game.getPlayers();
    }

    public Player getCurrentPlayer(String gameId) {
        Game game = getGame(gameId);
        if (game == null) {
            return null;
        }

        return game.getCurrentPlayer();
    }

    public Player getPlayerWithMutex(String gameId) {
        Game game = getGame(gameId);
        if (game == null) {
            return null;
        }
        return game.getMutexPlayer();
    }

    public void removeMutex(String gameId) {
        Game game = getGame(gameId);
        if (game == null) {
            return;
        }
        game.setMutexPlayer(null);
    }

    public MutexStatus setMutex(String gameId, Player player) {
        Game game = getGame(gameId);
        if (game == null) {
            return MutexStatus.FAILED;
        }

        synchronized (game) {
            if (game.getMutexPlayer() == null) {
                game.setMutexPlayer(player);
                return MutexStatus.SUCCESS;
            } else if (game.getMutexPlayer().equals(player)) {
                return MutexStatus.ALREADY_HOLDING;
            } else {
                return MutexStatus.FAILED;
            }
        }
    }
}
