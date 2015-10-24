package io.github.flbaue.vs.RESTopoly;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashSet;
import java.util.Set;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by florian on 24.10.15.
 */
public class JailService {

    private Set<Player> playerSet = new HashSet<>();
    private Gson gson = new Gson();

    public static void main(String[] args) {
        new JailService().run();
    }

    private void run() {
        System.out.println("Jail Service is starting");

        post("/jail", this::createPlayer);
        get("/jail/:playerId", this::getPlayer);
        post("/jail/:playerId", this::playerRollsDice);
    }

    private Object createPlayer(Request request, Response response) throws Exception {
        Player player = gson.fromJson(request.body(), Player.class);
        playerSet.add(player);
        response.status(201);
        player.setIsInJail(true);
        return gson.toJson(player);
    }

    private Object getPlayer(Request request, Response response) throws Exception {
        for (Player player : playerSet) {
            if (player.getPlayerId().equals(request.params(":playerId"))) {
                return gson.toJson(player);
            }
        }
        response.status(404);
        return gson.toJson(new Player("not found"));
    }

    private Object playerRollsDice(Request request, Response response) throws Exception {
        String playerId = request.params("playerId");
        boolean pasch = Boolean.parseBoolean(request.queryParams("pasch"));

        Player player = null;
        for (Player p : playerSet) {
            if (p.getPlayerId().equals(playerId)) {
                player = p;
            }
        }

        if (player == null) {
            response.status(404);
            return gson.toJson(new Player("not found"));

        } else {

            if (player.getRoundsInJail() == 3) {
                response.status(402);

            } else {
                if (pasch) {
                    playerSet.remove(player);
                    player.setIsInJail(false);

                } else {
                    player.addRound();
                }
            }

            return gson.toJson(player);
        }
    }
}
