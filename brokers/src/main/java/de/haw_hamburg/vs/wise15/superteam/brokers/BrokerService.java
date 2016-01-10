package de.haw_hamburg.vs.wise15.superteam.brokers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import spark.Request;
import spark.Response;

import javax.net.ssl.SSLContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.apache.http.conn.ssl.SSLContexts.custom;
import static spark.Spark.*;

/**
 * Created by masha on 28.11.15.
 */
public class BrokerService {

    private String ip;
    Gson gson = new Gson();
    Broker broker = new Broker();

    public static void main(String[] args) throws Exception {

        new BrokerService().run();
    }

    private void run() throws Exception {
        System.out.println("BrokerService is starting");

        register();

        //Gets a broker
        get("/broker/:gameid", this::getBroker);

        //List of available place
        get("/broker/:gameid/places", this::getListOfPlaces);

        //Gets a places
        get("/broker/:gameid/places/:placeid", this::getPlace);

        ///boards einen Broker pro Spiel erstellt
        put("/broker/:gameid", this::addGame);

        ///boards die verfügbaren Grundstücke registriert mit
        put("/broker/:gameid/places/:placeid", this::registerPlace);

        ///boards bei /brokers Besuche durch Spieler anmeldet
        post("/broker/:gameid/places/:placeid/visit/:playerid", this::visitPlace);

        //Gets a owner
        get("/broker/:gameid/places/:placeid/owner", this::getOwner);

        //Trade the place - changing the owner
        put("/broker/:gameid/places/:placeid/owner", this::changeOwner);

        //Spieler Grundstücke kaufen können durch
        post("/broker/:gameid/places/:placeid/owner", this::buyPlace);

        //takes a hypothecary credit onto the place
        put("/broker/:gameid/places/:placeid/hypothecarycredit", this::createHypothec);

        //removes the hypothecary credit from the place
        delete("/broker/:gameid/places/:placeid/hypothecarycredit", this::deletehypothek);

        //brokers bei fälliger Miete oder Erwerb dies der Bank meldet

    }

    private void register() throws Exception {

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
                .body(json.toString()).asString();
    }

    //delete http://localhost:4567/broker/:gameid/places/:placeid/hypothecarycredit
    private Object deletehypothek(Request request, Response response) throws UnirestException {
        String gameId = request.params(":gameid");
        String placeId = request.params(":placeid");
        ArrayList<Estate> estate = broker.getAllEstates().get(gameId);
        if (estate == null) {
            response.status(404);
            return gameId + " die gameid hat keine places";
        } else {
            for (Estate e : estate) {
                if (e.getPlace().getPlaceId().equals(placeId)) {
                    response.status(200);
                    e.setHypothek(false);
                    for (Game game : broker.getGames()) {
                        if (game.getGameId().equals(gameId)) {
                            Event event = new Event("hypothek ", "hypothek", "hypothek", "uri", game.getPlayerById(e.getOwner()));
                            //  post("/player/event", this::playerEvent);
                            List<Event> array = new ArrayList<>();
                            array.add(event);
                            Unirest.post("/player/event").
                                    header("Content-Type", "application/json")
                                    .body(gson.toJson(array)).asJson();    //oder asString?
                            response.status(200);
                            response.body(gson.toJson(event));
                            return response;
                        }
                    }
                    return "Keine Hypothek mehr vorhanden";
                }
            }


        }
        return null;
    }

    //put http://localhost:4567/broker/:gameid/places/:placeid/hypothecarycredit
    private Object createHypothec(Request request, Response response) throws UnirestException {
        String gameId = request.params(":gameid");
        String placeId = request.params(":placeid");
        ArrayList<Estate> estate = broker.getAllEstates().get(gameId);
        if (estate == null) {
            response.status(404);
            return gameId + " die gameid hat keine places";
        } else {
            for (Estate e : estate) {
                if (e.getPlace().getPlaceId().equals(placeId)) {
                    response.status(200);
                    e.setHypothek(true);
                    for (Game game : broker.getGames()) {
                        if (game.getGameId().equals(gameId)) {
                            Event event = new Event("hypothek ", "hypothek", "hypothek", "uri", game.getPlayerById(e.getOwner()));
                            //  post("/player/event", this::playerEvent);
                            List<Event> array = new ArrayList<>();
                            array.add(event);
                            Unirest.post("/player/event").
                                    header("Content-Type", "application/json")
                                    .body(gson.toJson(array)).asJson();    //oder asString?
                            response.status(200);
                            response.body(gson.toJson(event));
                            return response;
                        }
                    }
                    return "Hypothek vorhanden";
                }
            }


        }
        return null;
    }


    //put http://localhost:4567/broker/:gameid/places/:placeid/owner
    private Object changeOwner(Request request, Response response) throws UnirestException {
        String gameId = request.params(":gameid");
        String placeId = request.params(":placeid");
        String playerId = response.body();
        String oldOwner;
        ArrayList<Estate> estate = broker.getAllEstates().get(gameId);
        if (estate == null) {
            response.status(404);
            return gameId + " die gameid hat keine places";
        } else {
            for (Estate e : estate) {
                if (e.getPlace().getPlaceId().equals(placeId)) {
                    oldOwner = e.getOwner();
                    e.setOwner(playerId);
                    for (Game game : broker.getGames()) {
                        if (game.getGameId().equals(gameId)) {
                            Event event = new Event("change owner ", "change owner", "change owner", "uri", game.getPlayerById((oldOwner)));
                            //  post("/player/event", this::playerEvent);
                            List<Event> array = new ArrayList<>();
                            array.add(event);
                            Unirest.post("/player/event").
                                    header("Content-Type", "application/json")

                                    .body(gson.toJson(array)).asJson();    //oder asString?
                            response.status(200);
                            response.body(gson.toJson(event));
                            return response;
                        }
                    }
                }

            }
            response.status(400);
            return null;
        }
    }

    //get http://localhost:4567/broker/:gameid/places/:placeid/owner
    private Object getOwner(Request request, Response response) throws UnirestException {
        String gameId = request.params(":gameid");
        String placeId = request.params(":placeid");
        String oldOwner;
        ArrayList<Estate> estate = broker.getAllEstates().get(gameId);
        if (estate == null) {
            response.status(404);
            return gameId + " die gameid hat keine places";
        } else {
            for (Estate e : estate) {
                if (e.getPlace().getPlaceId().equals(placeId)) {
                    oldOwner = e.getOwner();
                    response.status(200);
                    response.body(gson.toJson(oldOwner));
                    return response;
                }
            }
        }


        response.status(404);
        return null;
    }


    //get http://localhost:4567/broker/:gameid/places/:placeid
    private Object getPlace(Request request, Response response) {
        String gameId = request.params(":gameid");
        String placeId = request.params(":placeid");
        ArrayList<Place> places = broker.getAllPlaces().get(gameId);
        if (places == null) {
            response.status(404);
            return gameId + " die gameid hat keine places";
        } else {
            for (Place place : places) {
                if (place.getPlaceId().equals(placeId)) {
                    response.status(200);
                    response.body(gson.toJson(place));
                    return response;
                }
            }
        }
        response.status(404);
        return placeId + " gibt es nicht in der Liste bei der gameId: " + gameId;
    }

    //get http://localhost:4567/broker/:gameid/places
    private Object getListOfPlaces(Request request, Response response) {
        String gameId = request.params(":gameid");
        ArrayList<Estate> estates = broker.getAllEstates().get(gameId);
        if (estates == null) {
            response.status(404);
            return "Bei gameId: " + gameId + " wurden keine estates gefunden";
        } else {
            response.status(200);
            response.body(gson.toJson(estates));
            return response;
        }

    }

    //get http://localhost:4567/broker/:gameid
    private Object getBroker(Request request, Response response) {
        String gameId = request.params(":gameid");
        for (Game game : broker.getGames()) {
            if (game.getGameId().equals(gameId)) {
                response.status(200);
                response.body(gson.toJson(broker));
                return response;
            }
        }
        response.status(404);
        return null;
    }

    //post http://localhost:4567/broker/:gameid/places/:placeid/owner
    private Object buyPlace(Request request, Response response) throws UnirestException {
        String gameId = request.params(":gameid");
        String playerId = request.body();
        String placeId = request.params(":placeid");
        ArrayList<Estate> estatesToGame;
        boolean sold;
        estatesToGame = broker.getAllEstates().get(gameId);
        if (estatesToGame.size() == 0) return "keine registrierten Grundstücke bei GameId: " + gameId;
        if (!estatesToGame.contains(placeId)) return "Grundstück: " + placeId + " existiert nicht bei game: " + gameId;
        for (Estate estate : estatesToGame) {
            //es gibt einen besitzer und das ist nicht die übergebene playerid -> miete zahlen
            if (estate.getPlace().getPlaceId().equals(placeId)) {
                if (estate.getOwner().equals("")) {
                    Integer cost = estate.getCost();
                    //  "/banks/:gameid/transfer/from/:from/:amount"
                    HttpResponse<JsonNode> s = Unirest.post("/banks/" + gameId + "/transfer/from/" + playerId + "/" + cost).asJson();
                    if (s.getStatus() == 200) {
                        estate.setOwner(placeId);
                        for (Game game : broker.getGames()) {
                            if (game.getGameId().equals(gameId)) {
                                Event event = new Event("buy", "buy", "buy", "uri", game.getPlayerById(playerId));
                                //  post("/player/event", this::playerEvent);
                                List<Event> array = new ArrayList<>();
                                array.add(event);
                                Unirest.post("/player/event").
                                        header("Content-Type", "application/json")
                                        .body(gson.toJson(array)).asJson();    //oder asString?
                                response.status(200);
                                response.body(gson.toJson(event));
                                return response;
                            } else {
                                response.status(400);
                                return "in der liste von games in estate wurde keine gameid gefunden";
                            }
                        }


                    } else {
                        response.status(400);
                        return "das geld wurde nicht überwiesen";

                    }
                } else {
                    response.status(400);
                    return "der place hat schon einen besitzer";
                }
            } else {
                response.status(404);
                return "es wurde kein place gefunden unter der placeid: " + placeId;
            }

        }
        return null;
    }


    //post http://localhost:4567/broker/:gameid/places/:placeid/visit/:playerid
    private Object visitPlace(Request request, Response response) throws UnirestException {
        String gameId = request.params(":gameid");
        String playerId = request.params(":playerid");
        String placeId = request.params(":placeid");
        ArrayList<Estate> estatesToGame = new ArrayList<>();
        boolean sold;
        estatesToGame = broker.getAllEstates().get(gameId);
        if (estatesToGame.size() == 0) return "keine registrierten Grundstücke bei GameId: " + gameId;
        if (!estatesToGame.contains(placeId)) return "Grundstück: " + placeId + " existiert nicht bei game: " + gameId;
        for (Estate estate : estatesToGame) {
            //es gibt einen besitzer und das ist nicht die übergebene playerid -> miete zahlen
            if (estate.getPlace().getPlaceId().equals(placeId)) {
                if (!estate.getOwner().equals("") && !estate.getOwner().equals(playerId)) {
                    Integer miete = estate.getRent();
                    //post("/banks/:gameid/transfer/from/:from/to/:to/:amount",)
                    HttpResponse<JsonNode> s = Unirest.post("/banks/" + gameId + "/transfer/from/" + playerId + "/to/" + estate.getOwner() + "/" + miete).asJson();
                    if (s.getStatus() == 200) {
                        for (Game game : broker.getGames()) {
                            if (game.getGameId().equals(gameId)) {
                                Event event = new Event("rent", "rent", "rent", "uri", game.getPlayerById(playerId));
                                //  post("/player/event", this::playerEvent);
                                List<Event> array = new ArrayList<>();
                                array.add(event);
                                Unirest.post("/player/event").
                                        header("Content-Type", "application/json")
                                        .body(gson.toJson(array)).asJson();    //oder asString?
                                response.status(200);
                                response.body(gson.toJson(event));
                                return response;
                            }
                        }


                    }
                }
            }
        }

        //if ()


        //ist der place schon verkauft?
        //ja-> gehört der lace dem playerid ja-> nichts wird pasieren
        //nein-> miete zahlen
        //bezahlt-> ein event an den besitzer schicken, dass die miete bezahlt wurde
        //nein->kann der playerid diesen platz kaufen
        return null;
    }

    //put http://localhost:4567/broker/:gameid/places/:placeid
    private Object registerPlace(Request request, Response response) {
        try {
            String placeid = request.params(":placeid");
            String gameId = request.params(":gameid");
            broker.addPlace(placeid, gameId);
            Estate estate = gson.fromJson(request.body(), Estate.class);
            broker.addEstate(gameId, estate);
            return "Game wurde registriert im Broker";
        } catch (JsonSyntaxException e) {
            return "JsonSyntaxException from put broker/gameId";
        }

    }

    //put http://localhost:4567/broker/:gameid
    private Object addGame(Request request, Response response) {
        String gameId = request.params(":gameid");
        try {
            Game game = gson.fromJson(request.body(), Game.class);
            broker.addGame(game);
            response.status(200);
            return "Game wurde registriert im Broker";
        } catch (JsonSyntaxException e) {
            return "JsonSyntaxException from put broker/gameId";
        }
    }
}