package io.github.flbaue.vs.RESTopoly;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static spark.Spark.*;

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

        post("/jail", (request, response) -> {
            Player player = gson.fromJson(request.body(), Player.class);
            playerSet.add(player);
            response.status(201);
            player.setIsInJail(true);
            return gson.toJson(player);
        });


        get("/jail/:playerId", ((request, response) -> {
            for (Player player : playerSet) {
                if (player.getPlayerId().equals(request.params(":playerId"))) {
                    return gson.toJson(player);
                }
            }
            response.status(404);
            return gson.toJson(new Player("not found"));
        }));


        post("/jail/:playerId", (request, response) -> {
            String playerId = request.params("playerId");
            boolean pasch = Boolean.parseBoolean(request.queryParams("pasch"));

            if (!playerSet.contains(new Player(playerId))) {
                response.status(404);
                return gson.toJson(new Player("not found"));
            }

            Player player = null;
            for (Player p : playerSet) {
                if (p.getPlayerId().equals(request.params(":playerId"))) {
                    player = p;
                }
            }

            if (player.getRoundsInJail() == 3) {
                response.status(402);
                return gson.toJson(player);
            }

            if (pasch == true) {
                playerSet.remove(player);
                player.setIsInJail(false);
                return gson.toJson(player);
            } else {
                player.addRound();
                return gson.toJson(player);
            }

        });
    }
}
