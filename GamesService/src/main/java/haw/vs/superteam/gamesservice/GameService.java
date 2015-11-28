package haw.vs.superteam.gamesservice;

import com.google.gson.Gson;
import haw.vs.superteam.gamesservice.model.Game;
import haw.vs.superteam.gamesservice.model.MutexStatus;
import haw.vs.superteam.gamesservice.model.Player;
import haw.vs.superteam.gamesservice.model.PlayerCollection;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;


import java.util.Optional;

import static spark.Spark.*;

/**
 * Created by florian on 18.11.15.
 */
public class GameService {

    public static final String APPLICATION_JSON = "application/json";

    private GameController gameController = new GameController();


    public static void main(String[] args) {

        new GameService().run();
    }


    private void run() {

        JsonTransformer jsonTransformer = new JsonTransformer();

        get("/", this::root);

        get("/games", this::getAllGames, jsonTransformer);
        post("/games", this::createNewGame, jsonTransformer);

        get("/games/:gameId", this::getGame);

        get("/games/:gameId/players", this::getPlayersFromGame);

        get("/games/:gameId/players/:playerId", this::getPlayerFromGame);
        put("/games/:gameId/players/:playerId", this::addPlayerToGame);
        delete("/games/:gameId/players/:playerId", this::removePlayerFromGame);

        get("/games/:gameId/players/:playerId/ready", this::isPlayerReady);
        put("/games/:gameId/players/:playerId/ready", this::togglePlayerReady);

        get("/games/:gameId/players/current", this::getCurrentPlayer);

        get("/games/:gameId/players/turn", this::getPlayerWithMutex);
        put("/games/:gameId/players/turn", this::setMutext);
        delete("/games/:gameId/players/turn", this::removeMutex);
    }

    private Object removeMutex(Request request, Response response) {

        gameController.removeMutex(request.params(":gameId"));

        response.status(200);
        response.type(APPLICATION_JSON);
        return null;
    }

    private Object setMutext(Request request, Response response) {

        MutexStatus mutex = gameController.setMutexToPlayer(
                request.params(":gameId"),
                request.queryParams("player")
        );

        int status;
        switch (mutex) {
            case SUCCESS:
                status = 201;
                break;
            case FAILED:
                status = 409;
                break;
            default:
                status = 200;
                break;
        }

        response.status(status);
        response.type(APPLICATION_JSON);
        return null;
    }


    private Object getPlayerWithMutex(Request request, Response response) {

        Player player = gameController.getPlayWithMutex(request.params(":gameId"));

        response.status(200);
        response.type(APPLICATION_JSON);
        return player;
    }


    private Object getCurrentPlayer(Request request, Response response) {

        Player player = gameController.getCurrentPlayer(request.params(":gameId"));

        response.status(200);
        response.type(APPLICATION_JSON);
        return player;
    }


    private Object togglePlayerReady(Request request, Response response) {

        gameController.togglePlayerReady(
                request.params(":gameId"),
                request.params(":playerId")
        );

        response.status(200);
        response.type(APPLICATION_JSON);
        return null;
    }


    private Object isPlayerReady(Request request, Response response) {

        boolean ready = gameController.isPlayerReady(
                request.params(":gameId"),
                request.params(":playerId")
        );

        response.status(200);
        response.type(APPLICATION_JSON);
        return ready;
    }


    private Object removePlayerFromGame(Request request, Response response) {

        gameController.removePlayerFromGame(
                request.params(":gameId"),
                request.params(":playerId")
        );

        response.status(200);
        response.type(APPLICATION_JSON);
        return null;
    }


    private Object addPlayerToGame(Request request, Response response) {

        Boolean success = gameController.addPlayerToGame(
                request.params(":gameId"),
                request.params(":playerId"),
                request.queryParams("name"),
                request.queryParams("uri")
        );

        int status = (success) ? 200 : 400;
        response.status(status);
        response.type(APPLICATION_JSON);
        return null;
    }


    private Object getPlayerFromGame(Request request, Response response) {

        Optional<Player> player = gameController.getPlayerFromGame(
                request.params(":gameId"),
                request.params(":playerId")
        );

        int status = (player.isPresent()) ? 200 : 404;
        response.status(status);
        response.type(APPLICATION_JSON);
        return player;
    }


    private Object getPlayersFromGame(Request request, Response response) {

        Optional<PlayerCollection> players = gameController.getPlayersFromGame(request.params(":gameId"));

        int status = (players.isPresent()) ? 200 : 404;
        response.status(status);
        response.type(APPLICATION_JSON);
        return players.orElse(null);
    }


    private Object getGame(Request request, Response response) {

        Optional<Game> game = gameController.getGame(request.params(":gameId"));

        int status = (game.isPresent()) ? 200 : 404;
        response.status(status);
        response.type(APPLICATION_JSON);
        return game.orElse(null);
    }


    private Object createNewGame(Request request, Response response) {

        Optional<Game> game = gameController.createNewGame(request.body());

        int status = (game.isPresent()) ? 201 : 400;
        response.status(status);
        response.type(APPLICATION_JSON);
        return game.orElse(null);
    }


    private Object getAllGames(Request request, Response response) {

        response.status(200);
        response.type(APPLICATION_JSON);
        return gameController.getAll();
    }


    private Object root(Request request, Response response) {

        return "The method you have called ('" + request.pathInfo() + "') is currently not implemented.";
    }


    private class JsonTransformer implements ResponseTransformer {

        public static final String EMPTY = "";
        private Gson gson = new Gson();

        @Override
        public String render(Object model) {

            if (model != null) {
                return gson.toJson(model);
            } else {
                return EMPTY;
            }
        }
    }
}
