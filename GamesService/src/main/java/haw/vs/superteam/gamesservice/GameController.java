package haw.vs.superteam.gamesservice;

import haw.vs.superteam.gamesservice.api.BoardsAdapter;
import haw.vs.superteam.gamesservice.api.PlayerAdapter;
import haw.vs.superteam.gamesservice.model.*;

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
    private final String serviceURI;
    private final PlayerAdapter playerAdapter;
    private final BoardsAdapter boardsAdapter;
    private Set<Game> games = new HashSet<>();

    public GameController(String serviceURI, PlayerAdapter playerAdapter, BoardsAdapter boardsAdapter) {

        this.serviceURI = serviceURI;
        this.playerAdapter = playerAdapter;
        this.boardsAdapter = boardsAdapter;
    }

    public GameCollection getAll() {

        return new GameCollection(new ArrayList<>(games));
    }


    public Game createNewGame(Components components) {

        Game game = new Game(String.valueOf(gameCounter.incrementAndGet()), components);
        games.add(game);

        game.setUri(game.getComponents().getGame() + "/games/" + game.getGameid());

        Board board = boardsAdapter.createBoard(game);
        if (board != null) {
            return game;
        } else {
            games.remove(game);
            return null;
        }
    }


    public Game getGame(String gameId) {
        return games.stream()
                .filter(g -> g.getGameid().equals(gameId))
                .findAny()
                .orElse(null);
    }


    public Boolean addPlayerToGame(String gameId, String playerId, String playerName, String playerURI) {
        Game game = getGame(gameId);
        if (game == null) {
            return false;
        }

        Player player = new Player(playerId, playerName, playerURI);
        boolean playerAdded = game.addNewPlayer(player);
        if (playerAdded) {

            if (boardsAdapter.addPlayer(game, player)) {
                logger.info("Player " + playerId + " added to board");
                return true;
            } else {
                logger.severe("Player could not be added to the board");
                return false;
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

            Game game = getGame(gameId);
            if (game.startGame()) {
                playerAdapter.gameStart(game);
                playerAdapter.turn(player);
            }
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
