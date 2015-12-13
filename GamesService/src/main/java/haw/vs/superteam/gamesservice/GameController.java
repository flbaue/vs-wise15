package haw.vs.superteam.gamesservice;

import com.google.gson.Gson;
import haw.vs.superteam.gamesservice.api.BoardsAPI;
import haw.vs.superteam.gamesservice.model.*;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by florian on 18.11.15.
 */
public class GameController {

    private static AtomicLong gameCounter = new AtomicLong(0);
    private Gson gson = new Gson();
    private Set<Game> games = new HashSet<>();
    private Components components;
    private BoardsAPI boardsAPI;

    public GameController() {
        components = new Components();
        //TODO set component paths

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVICE_DIRECTORY_URL + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(Utils.getUnsafeOkHttpClient())
                .build();

        boardsAPI = retrofit.create(BoardsAPI.class);
    }

    public GameCollection getAll() {

        return new GameCollection(new ArrayList<>(games));
    }


    public Game createNewGame() {


        Game game = new Game(String.valueOf(gameCounter.incrementAndGet()));
        games.add(game);

        game.setComponents(components);
        game.setUri("");
        //TODO set URI?

//        try {
//            Response<Board> response = boardsAPI.createBoard(game.getGameid()).execute();
//            if(response.isSuccess()) {
//                game.getComponents().setBoard(boardsAPI.);
//            }
//        }
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

        return game.addNewPlayer(new Player(playerId, playerName, playerURI));
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
