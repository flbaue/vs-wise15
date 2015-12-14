package haw.vs.superteam.gamesservice;

import com.google.gson.Gson;
import haw.vs.superteam.gamesservice.api.BoardsAPI;
import haw.vs.superteam.gamesservice.api.ServicesAPI;
import haw.vs.superteam.gamesservice.model.*;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
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
    private final ServicesAPI servicesAPI;
    private GameController gameController;
    private Gson gson = new Gson();
    private String serviceURI;

    public GameService() throws IOException {

        String ip = InetAddress.getLocalHost().getHostAddress();
//        serviceURI = "https://vs-docker.informatik.haw-hamburg.de/cnt/" + ip + "/4567";
        serviceURI = "https://vs-docker.informatik.haw-hamburg.de/ports/15321";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVICE_DIRECTORY_URL + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(Utils.getUnsafeOkHttpClient())
                .build();

        servicesAPI = retrofit.create(ServicesAPI.class);


        ServiceLocator serviceLocator = new ServiceLocator(servicesAPI);
        Service boardsService = serviceLocator.getBoardsService();
        String boardsServiceURI = boardsService.getUri();

        Retrofit boardsServiceRetrofit = new Retrofit.Builder()
                .baseUrl(boardsServiceURI + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(Utils.getUnsafeOkHttpClient())
                .build();

        BoardsAPI boardsAPI = boardsServiceRetrofit.create(BoardsAPI.class);

        Components components = new Components();
        components.setGame(serviceURI + "/games");
        components.setBoard(boardsServiceURI);
        //TODO set component paths

        gameController = new GameController(components, boardsAPI);
    }

    public static void main(String[] args) throws IOException {

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
        log.setLevel(Level.ALL);
        new GameService().run();
    }

    private void run() throws IOException {

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

    private void registerService() throws IOException {
        retrofit.Response<Void> response = servicesAPI.registerService(new Service(
                "SuperTeamGamesService",
                "Games Service of SuperTeam",
                "games",
                serviceURI)).execute();

        if (response.isSuccess()) {
            System.out.println("GamesService registration succeeded!");
        } else {
            System.out.println("GamesService registration failed!");
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
