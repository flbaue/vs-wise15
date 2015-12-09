package haw.vs.superteam.gamesservice;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import haw.vs.superteam.gamesservice.model.Game;
import haw.vs.superteam.gamesservice.model.MutexStatus;
import haw.vs.superteam.gamesservice.model.Player;
import haw.vs.superteam.gamesservice.model.PlayerCollection;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static spark.Spark.*;

/**
 * Created by florian on 18.11.15.
 */
public class GameService {

    private static final String APPLICATION_JSON = "application/json";
    private static final Logger log = Logger.getLogger(GameService.class.getName());
    private GameController gameController = new GameController();
    private Gson gson = new Gson();


    public static void main(String[] args) {

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
        log.setLevel(Level.ALL);
        new GameService().run();
    }


    private void run() {

        JsonTransformer jsonTransformer = new JsonTransformer();

        get("/", this::root);

        get("/games", this::getAllGames, jsonTransformer);
        post("/games", this::createNewGame, jsonTransformer);

        get("/games/:gameId", this::getGame, jsonTransformer);

        get("/games/:gameId/players", this::getPlayersFromGame, jsonTransformer);

        get("/games/:gameId/players/:playerId", this::getPlayerFromGame, jsonTransformer);
        put("/games/:gameId/players/:playerId", this::addPlayerToGame, jsonTransformer);
        delete("/games/:gameId/players/:playerId", this::removePlayerFromGame, jsonTransformer);

        get("/games/:gameId/players/:playerId/ready", this::isPlayerReady);
        put("/games/:gameId/players/:playerId/ready", this::togglePlayerReady, jsonTransformer);

        get("/games/:gameId/players/current", this::getCurrentPlayer, jsonTransformer);

        get("/games/:gameId/players/turn", this::getPlayerWithMutex, jsonTransformer);
        put("/games/:gameId/players/turn", this::setMutext, jsonTransformer);
        delete("/games/:gameId/players/turn", this::removeMutex, jsonTransformer);

        registerService();
    }

    private void registerService() {
        //https://vs-docker.informatik.haw-hamburg.de/cnt/172.17.0.38/4567/dice
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();

            String uri = "https://vs-docker.informatik.haw-hamburg.de/cnt/" + ip + "/4567";
            OkHttpClient client = Utils.getUnsafeOkHttpClient();

            RequestBody body = RequestBody.create(MediaType.parse(
                    "application/json"),
                    "{" +
                            "\"name\": \"SuperGames\", " +
                            "\"description\": \"GamesService of team superteam\", " +
                            "\"service\": \"games\", " +
                            "\"uri\": \"" + uri + "\"" +
                            "}");

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .post(body)
                    .url("https://vs-docker.informatik.haw-hamburg.de/ports/8053/services")
                    .build();


            com.squareup.okhttp.Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.err.print("GamesService is registered");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Object setMutext(Request request, Response response) {

        Player player = gson.fromJson(request.body(), Player.class);
        MutexStatus mutexStatus = gameController.setMutex(request.params(":gameId"), player);

        int status;
        switch (mutexStatus) {
            case SUCCESS:
                status = 201;
                break;
            case FAILED:
                status = 409;
                break;
            default:
                status = 200;
        }
        response.status(status);
        response.type(APPLICATION_JSON);
        return player;
    }


    private Object removeMutex(Request request, Response response) {
        gameController.removeMutex(request.params(":gameId"));
        return null;
    }


    private Object getPlayerWithMutex(Request request, Response response) {
        Player player = gameController.getPlayerWithMutex(request.params(":gameId"));

        int status = (player != null) ? 200 : 404;
        response.status(status);
        response.type(APPLICATION_JSON);
        return player;
    }

    private Object getCurrentPlayer(Request request, Response response) {
        Player player = gameController.getCurrentPlayer(request.params(":gameId"));

        int status = (player != null) ? 200 : 404;
        response.status(status);
        response.type(APPLICATION_JSON);
        return player;
    }


    private Object getPlayersFromGame(Request request, Response response) {
        PlayerCollection playerCollection = gameController.getPlayersFromGame(request.params(":gameId"));

        int status = (playerCollection != null) ? 200 : 404;
        response.status(status);
        response.type(APPLICATION_JSON);
        return playerCollection;
    }


    private Object getGame(Request request, Response response) {
        Game game = gameController.getGame(request.params(":gameId"));

        int status = (game != null) ? 200 : 404;
        response.status(status);
        response.type(APPLICATION_JSON);
        return game;
    }


    private Object isPlayerReady(Request request, Response response) {
        Boolean status = gameController.isPlayerReady(request.params(":gameId"), request.params(":playerId"));
        return status;
    }


    private Object removePlayerFromGame(Request request, Response response) {
        gameController.removePlayerFromGame(request.params(":gameId"), request.params(":playerId"));
        return null;
    }


    private Object getPlayerFromGame(Request request, Response response) {
        Player player = gameController.getPlayerFromGame(request.params(":gameId"), request.params(":playerId"));

        int status = (player != null) ? 200 : 400;
        response.status(status);
        response.type(APPLICATION_JSON);
        return player;
    }


    private Object togglePlayerReady(Request request, Response response) {
        gameController.togglePlayerReady(request.params(":gameId"), request.params(":playerId"));
        return null;
    }


    private Object addPlayerToGame(Request request, Response response) {
        log.fine("adding player " + request.params(":playerId") + " to game " + request.params(":gameId"));
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


    private Object createNewGame(Request request, Response response) {
        log.fine("creating new game");

        Game game = gameController.createNewGame();

        log.fine("game " + game.getGameid() + " created");

        response.status(200);
        response.type(APPLICATION_JSON);
        return game;
    }


    private Object getAllGames(Request request, Response response) {
        log.fine("getting all games");
        response.status(200);
        response.type(APPLICATION_JSON);
        return gameController.getAll();
    }


    private Object root(Request request, Response response) {

        return "I am the GameService of team superteam";
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
