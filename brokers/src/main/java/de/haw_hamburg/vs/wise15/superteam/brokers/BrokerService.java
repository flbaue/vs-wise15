package de.haw_hamburg.vs.wise15.superteam.brokers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.Unirest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import spark.Request;
import spark.Response;

import javax.net.ssl.SSLContext;

import static org.apache.http.conn.ssl.SSLContexts.custom;
import static spark.Spark.*;

/**
 * Created by masha on 28.11.15.
 */
public class BrokerService {

    Gson gson = new Gson();
    Broker broker;

    public static void main(String[] args) throws Exception {

        new BrokerService().run();
    }

    private void run() throws Exception {
        System.out.println("BrokerService is starting");

        register();

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

    private void register() throws Exception{

        JsonObject json = new JsonObject();
        json.addProperty("name", "SuperTeamBrokersService");
        json.addProperty("description", "BrokersService von SuperTeam");
        json.addProperty("service", "brokers");
        json.addProperty("uri", "https://vs-docker.informatik.haw-hamburg.de/ports/15326");
        SSLContext sslcontext = null;
        sslcontext = custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        Unirest.setHttpClient(httpclient);
        Unirest.post("https://vs-docker.informatik.haw-hamburg.de/ports/8053/services")
                .header("Content-Type", "application/json")
                .body(json.toString()).asJson();
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
        if (broker.getBrokerId().equals(request.params(":gameId"))){
            response.status(200);
            response.body(gson.toJson(broker));
            return response;
        }
        response.status(404);
        return null;
    }

    private Object getListOfPlaces(Request request, Response response) {
        return null;
    }

    private Object getBroker(Request request, Response response) {
        if (broker.getBrokerId().equals(request.params(":gameId"))){
            response.status(200);
            response.body(gson.toJson(broker));
            return response;
        }
        response.status(404);
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
        String gameId = request.params(":gameId");
        try {
            Game game = gson.fromJson(request.body(), Game.class);
            broker = new Broker(gameId, game);
            response.status(201);
            return null;
        }catch(JsonSyntaxException e){
            return "JsonSyntaxException from put broers/gameId";
        }
    }
}
