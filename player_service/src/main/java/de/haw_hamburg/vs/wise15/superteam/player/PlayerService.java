package de.haw_hamburg.vs.wise15.superteam.player;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class PlayerService {

    private Gson gson = new Gson();
    //ToDo: player parm!?
    private Player player = new Player();

    public static void main(String[] args) {



        new PlayerService().run();
    }

    private void run() {
        System.out.println("PlayerService is starting");

        //der Spieler benachrichtigt wird, wenn er am Zug ist per post /player/turn
        post("/player/turn", this::notifyPlayerTurn);

        //dem Spieler Ereignisse zugestellt werden können mit post /player/event
        post("/player/event", this::notifyPlayerEvent);

        //get the details about a player
        get("/player", this::getDetails);

    }

    private Object getDetails(Request request, Response response) {

        return null;
    }

    private Object notifyPlayerEvent(Request request, Response response) {
        //ToDo: im request wird ein player mitübergeben!?
        return null;
    }

    private Object notifyPlayerTurn(Request request, Response response) {
        player.setTurn(true);
        //ToDo: muss der player jetzt würfeln?
        return null;
    }
}


