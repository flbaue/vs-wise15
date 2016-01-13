package de.haw_hamburg.vs.wise15.superteam.events;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.Unirest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import spark.Request;
import spark.Response;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static spark.Spark.*;

/**
 * Created by masha on 28.11.15.
 */
public class EventService {

    Gson gson = new Gson();
    Map<String, List<Event>> events = new HashMap<>();
    List<Subscription> subsList = new ArrayList<Subscription>();
    private AtomicInteger subsId = new AtomicInteger(0);
    private AtomicInteger eventId = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        new EventService().run();
    }

    private void run() throws Exception {
        //anmeldung
        //https://vs-docker.informatik.haw-hamburg.de/ports/8053/services
        register();
        System.out.println("EventsService is starting");

        //List of available event by gameId
        //ok, bis auf die Benachrichtigung
        post("/events", this::createEvents);

        //List of available event by gameId
        //ok
        get("/events", this::getEvents);

        //alle events bei gameId löschen
        //ok
        delete("/events", this::deleteEvent);

        //List of available subscription
        //ok
        get("/events/subscriptions", this::getEventSubscriptions);

        //gets the event details
        //ok
        get("/events/:eventId", this::getEventDetails);

        //removes the subscription from the service
        //ok
        delete("/events/subscriptions/subscriptions/:subscription", this::deleteEventSubscriptions);

        //alle Interessenten benachrichtigt werden über neue Events
        //ok
        post("/events/subscriptions", this::postEventSubscriptions);

    }

    //delete http://localhost:4567/events?gameId
    private Object deleteEvent(Request request, Response response) {
        String gameId = request.queryString();
        List<Event> events = this.events.get(gameId);

        if (events != null) {
            this.events.get(gameId).clear();
            response.status(200);
            return "erfolgreich gelöscht";
        } else {
            response.status(404);
            return "bei gameId keine eventliste";
        }
    }

    private void register() throws Exception {

        JsonObject json = new JsonObject();
        json.addProperty("name", "SuperTeamEventsService");
        json.addProperty("description", "Event Service von SuperTeam");
        json.addProperty("service", "events");
        json.addProperty("uri", "https://vs-docker.informatik.haw-hamburg.de/ports/15325");
        SSLContext sslcontext = null;
        sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        Unirest.setHttpClient(httpclient);
        //sout
        //System.out.println(json.toString());
        Unirest.post("https://vs-docker.informatik.haw-hamburg.de/ports/8053/services")
                .header("Content-Type", "application/json")
                .body(json.toString()).asString();

    }

    //post http://localhost:4567/events/subscriptions subscription in body
    private Object postEventSubscriptions(Request request, Response response) {
        try {
            Subscription subscription = gson.fromJson(request.body(), Subscription.class);
            subscription.setId(Integer.toString(subsId.incrementAndGet()));
            subsList.add(subscription);
            response.status(201);
            return "subscription " + subscription.getId();
        } catch (JsonSyntaxException e) {
            return "JsonSyntaxException von post events/subscriptions";
        }
    }

    //post http://localhost:4567/events?gameId event in body
    private Object createEvents(Request request, Response response) throws Exception {
        System.out.println("Received event raw: " + request.body() + " with gameId: " + request.queryString());
        String gameId = request.queryParams("gameId");
        List<Event> ary;
        try {
            Event event = gson.fromJson(request.body(), Event.class);
            event.setId(Integer.toString(eventId.incrementAndGet()));
            ary = events.get(gameId);
            if (ary != null) {
                ary.add(event);

                events.put(gameId, ary);
                //subscription durchgehen und uri von subsc das array von events in body zuschicken
                for (Subscription s : subsList) {
                    if (event.matchesEvent(s)) {
                        //post(uri von subscroption und event in array von body)
                        String body = gson.toJson(new Event[]{event});
                        System.out.println("Sending event to " + s.getUri());
                        Unirest.post(s.getUri())
                                .header("Content-Type", "application/json")
                                .body(body).asString();
                    }
                }

            } else {
                ary = new ArrayList<Event>();
                ary.add(event);
                events.put(gameId, ary);
                for (Subscription s : subsList) {
                    if (event.matchesEvent(s)) {
                        String body = gson.toJson(ary);
                        System.out.println("Sending event to " + s.getUri());
                        Unirest.post(s.getUri())
                                .header("Content-Type", "application/json")
                                .body(body).asString();
                    }
                }
            }
            response.status(201);
            response.header("Location","/events/" + event.getId());
            return "";

        } catch (JsonSyntaxException e) {
            return "JsonSyntaxException bei post/events";
        }
    }

    //delete http://localhost:4567/events/subscriptions/subscriptions/{subscription}
    private Object deleteEventSubscriptions(Request request, Response response) {
        String subId = request.params(":subscription");
        for (Subscription s : subsList) {
            if (s.getId().equals(subId)) {
                subsList.remove(s);
                response.status(200);
                return "gelöscht";
            }
        }
        response.status(404);
        return "subscriptionId nicht gefunden";
    }

    //get http://localhost:4567/events/subscriptions
    private Object getEventSubscriptions(Request request, Response response) {
        String s = gson.toJson(subsList);
        response.status(200);
        return s;
    }

    // get http://localhost:4567/events/1
    private Object getEventDetails(Request request, Response response) {
        String eventId = request.params(":eventId");
        System.out.println(eventId);
        for (String key : events.keySet()) {
            for (Event element : events.get(key)) {
                if (element.getId().equals(eventId)) {
                    response.status(200);
                    return gson.toJson(element);
                }
            }
        }
        response.status(404);
        return "kein event bei eventId";
    }

    //get http://localhost:4567/events?gameId
    private Object getEvents(Request request, Response response) {
        String gameId = request.queryString();
        List<Event> events = this.events.get(gameId);

        if (events != null) {
            response.status(200);
            return gson.toJson(new EventsCollection(events));
        } else {
            response.status(404);
            return "bei gameId keine eventliste";
        }
    }
}
