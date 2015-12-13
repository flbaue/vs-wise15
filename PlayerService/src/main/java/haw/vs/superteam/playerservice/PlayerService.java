package haw.vs.superteam.playerservice;

import com.google.gson.Gson;
import haw.vs.superteam.playerservice.model.Event;
import haw.vs.superteam.playerservice.model.Player;
import spark.Request;
import spark.Response;

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
        playerController = new PlayerController();
    }

    public static void main(String[] args) {
        new PlayerService().run();
    }

    private void run() {
        get("", this::root);
        get("/", this::root);

        get("/player", this::player);
        post("/player/turn", this::playerTurn);
        post("/player/event", this::playerEvent);
    }

    private Object root(Request request, Response response) {
        return "I AM WEASEL!";
    }

    private Object player(Request request, Response response) {
        Player player = playerController.getPlayerDetails();

        response.type(CONTENT_TYPE_JSON);
        response.status(200);
        return gson.toJson(player);
    }

    private Object playerTurn(Request request, Response response) {
        playerController.playerTurn();

        response.type(CONTENT_TYPE_JSON);
        response.status(200);
        return "";
    }

    private Object playerEvent(Request request, Response response) {
        Event[] events = gson.fromJson(request.body(), Event[].class);
        playerController.playerEvent(events);

        response.type(CONTENT_TYPE_JSON);
        response.status(200);
        return "";
    }
}
