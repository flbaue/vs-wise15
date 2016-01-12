package haw.vs.superteam.playerservice;

import com.google.gson.Gson;
import haw.vs.superteam.playerservice.model.Client;
import haw.vs.superteam.playerservice.model.Event;
import haw.vs.superteam.playerservice.model.Player;
import spark.Request;
import spark.Response;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by florian on 13.12.15.
 */
public class PlayerService {

    public static final String CONTENT_TYPE_JSON = "application/json";
    private final Gson gson = new Gson();
    private final PlayerController playerController;


    public PlayerService() {
        playerController = new PlayerController(new ClientFacade());
    }

    public static void main(String[] args) {
        new PlayerService().run();
    }

    private void run() {
        get("", this::root);
        get("/", this::root);

        get("/player/:playerId", this::player);
        post("/player/:playerId/turn", this::playerTurn);
        post("/player/:playerId/event", this::playerEvent);
        post("/player", this::addPlayer);
        delete("/player/:playerId", this::deletePlayer);

        registerService();
    }

    private void registerService() {

        String name = "SuperTeamPlayersService";
    }

    private Object deletePlayer(Request request, Response response) {
        int playerId = Integer.parseInt(request.params("playerId"));
        playerController.deletePlayer(playerId);
        response.status(200);
        return "Player deleted";
    }

    private Object addPlayer(Request request, Response response) {

        Client client = gson.fromJson(request.body(), Client.class);
        int playerId = playerController.addPlayer(client);
        if (playerId >= 0) {
            response.status(200);
            return playerId;
        } else {
            response.status(400);
            return "";
        }
    }

    private Object root(Request request, Response response) {
        return "I AM WEASEL!";
    }

    private Object player(Request request, Response response) {
        int playerId = Integer.parseInt(request.params("playerId"));
        Player player = playerController.getPlayerDetails(playerId);

        if(player == null) {
            response.status(404);
            return "Player Not Found";
        }

        response.type(CONTENT_TYPE_JSON);
        response.status(200);
        return gson.toJson(player);
    }

    private Object playerTurn(Request request, Response response) {
        int playerId = Integer.parseInt(request.params("playerId"));
        playerController.playerTurn(playerId);

        response.type(CONTENT_TYPE_JSON);
        response.status(200);
        return "";
    }

    private Object playerEvent(Request request, Response response) {
        int playerId = Integer.parseInt(request.params("playerId"));
        System.out.println("Received event raw: " + request.body());
        Event[] events = gson.fromJson(request.body(), Event[].class);
        playerController.playerEvent(playerId, events);

        response.type(CONTENT_TYPE_JSON);
        response.status(200);
        return "";
    }
}
