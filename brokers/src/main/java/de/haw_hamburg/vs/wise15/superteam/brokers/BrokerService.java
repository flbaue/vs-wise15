package de.haw_hamburg.vs.wise15.superteam.brokers;

import spark.Request;
import spark.Response;

import static spark.Spark.*;

/**
 * Created by masha on 28.11.15.
 */
public class BrokerService {

    public static void main(String[] args) {

        new BrokerService().run();
    }

    private void run() {
        System.out.println("BrokerService is starting");

        //Gets a broker
        get("/brokers/{gameid}", this::getBroker);

        //List of available place
        get("/broker/{gameid}/places",this::getListOfPlaces);

        //Gets a places
        get("/broker/{gameid}/places/{placeid}", this::getPlace);



        ///boards einen Broker pro Spiel erstellt
        put("/brokers/{gameid}", this::createBroker);

        ///boards die verfügbaren Grundstücke registriert mit
        put("/brokers/{gameid}/places/{placeid}", this::registerPlace);

        ///boards bei /brokers Besuche durch Spieler anmeldet
        post("/brokers/{gameid}/places/{placeid}/visit/{playerid}",this::visitPlace);

        //Gets a owner
        get("/broker/{gameid}/places/{placeid}/owner", this::getOwner);

        //Trade the place - changing the owner
        put("/broker/{gameid}/places/{placeid}/owner", this::changeOwner);

        //Spieler Grundstücke kaufen können durch
        post("/brokers/{gameid}/places/{placeid}/owner", this::buyPlace);

        //takes a hypothecary credit onto the place
        put("/broker/{gameid}/places/{placeid}/hypothecarycredit", this::createHypothec);

        //removes the hypothecary credit from the place
        delete("/broker/{gameid}/places/{placeid}/hypothecarycredit", this::deletePlace);

        //brokers bei fälliger Miete oder Erwerb dies der Bank meldet


    }

    private Object deletePlace(Request request, Response response) {
        return null;
    }

    private Object createHypothec(Request request, Response response) {
        return null;
    }

    private Object changeOwner(Request request, Response response) {
        return null;
    }

    private Object getOwner(Request request, Response response) {
        return null;
    }

    private Object getPlace(Request request, Response response) {
        return null;
    }

    private Object getListOfPlaces(Request request, Response response) {
        return null;
    }

    private Object getBroker(Request request, Response response) {
        return null;
    }

    private Object buyPlace(Request request, Response response) {
        return null;
    }

    private Object visitPlace(Request request, Response response) {
        return null;
    }

    private Object registerPlace(Request request, Response response) {
        return null;
    }

    private Object createBroker(Request request, Response response) {
        return null;
    }
}
