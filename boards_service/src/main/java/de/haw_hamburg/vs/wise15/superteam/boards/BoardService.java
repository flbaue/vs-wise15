package de.haw_hamburg.vs.wise15.superteam.boards;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import static spark.Spark.*;
/**
 * Created by masha on 28.11.15.
 */
public class BoardService {

    private Gson gson = new Gson();

    public static void main(String[] args) {
        new BoardService().run();
    }

    private void run() {
        System.out.println("BoardService is starting");

        //der Client einen Würfelwurf übergeben kann mit
        post("/boards/{gameid}/players/{playerid}/roll", this::passDice);

        // die Position des Spielers vom Service verändert wird
        post("/boards/{gameid}/players/{playerid}/position", this::changePosition);

        // die Position des Spielers abgefragt werden kann mit
        get("/games/{gameid}/players/{playerid}", this::getPosition);

        // der Zustand des Brettes abgefragt werden kann mit
        get("/games/{gameid}", this::getStatus);

        //dem Spieler Ereignisse zugestellt werden können mit post /player/event
        post("/player/event", this::notifyPlayerEvent);

        //Der Mutex kann erworben werden mittels
        put("/games/{gameid}/turn", this::putTurn);

        //Es kann abgefragt werden, ob ein Spieler den Mutext hält mit
        get("/games/{gameid}/turn", this::getTurn);

        //Freigegeben wird der Mutex durch
        delete("/games/{gameid}/turn", this::deleteTurn);

        //Der Mutex sollte frei gegeben werden direkt bevor der Zug beendet wird durch
        put("/games/{gameid}/players/{playerid}/ready", this::setReady);

    }

    private Object setReady(Request request, Response response) {
        return null;
    }

    private Object deleteTurn(Request request, Response response) {
        return null;
    }

    private Object putTurn(Request request, Response response) {
        return null;
    }

    private Object getTurn(Request request, Response response) {
        return null;
    }

    private Object getStatus(Request request, Response response) {
        return null;
    }

    private Object getPosition(Request request, Response response) {
        return null;
    }

    private Object changePosition(Request request, Response response) {
        return null;
    }

    private Object passDice(Request request, Response response) {
        return null;
    }

    private Object notifyPlayerEvent(Request request, Response response) {

        return null;
    }

    private Object notifyPlayerTurn(Request request, Response response) {
        return null;
    }
}



