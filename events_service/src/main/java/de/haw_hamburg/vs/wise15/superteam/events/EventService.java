package de.haw_hamburg.vs.wise15.superteam.events;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * Created by masha on 28.11.15.
 */
public class EventService {

    Gson gson = new Gson();
    Map<String,ArrayList<Event>> events = new HashMap<>();
    Integer id;
    Integer subsId;
    ArrayList<Subscription> subsList = new ArrayList<Subscription>();
    public static void main(String[] args) {


        new EventService().run();
    }

    private void run() {
        System.out.println("PlayerService is starting");

        //List of available event
        post("/events", this::createEvents);

        //List of available event
        get("/events", this::getEvents);

        //gets the event details
        get("/events/{eventId}", this::getEventDetails);

        //List of available subscription
        get("/events/subscriptions", this::getEventSubscriptions);

        //removes the subscription from the service
        delete("/events/subscriptions/subscriptions/{subscription}", this::deleteEventSubscriptions);

        //alle Interessenten benachrichtigt werden über neue Events
        post("/events/subscriptions", this::postEventSubscriptions);

        //post (uri of the subscroption)
    }

    private Object postEventSubscriptions(Request request, Response response) {
        subsId++;
        try {
            Subscription subscription = gson.fromJson(request.body(), Subscription.class);
            subscription.setId(Integer.toString(subsId));
            subsList.add(subscription);
            response.status(201);
            return null;
        }catch(JsonSyntaxException e){
            return "JsonSyntaxException von post events/subscriptions";
        }
    }

    private Object createEvents(Request request, Response response) {
        //gameid als queryparm
        //event in body
        String gameId = request.queryString();
        ArrayList<Event> ary;
        id ++;
        try{
            Event event = gson.fromJson(request.body(), Event.class);
            event.setId(Integer.toString(id));
            //Todo: wann wird die map ausgefüllt!?
            ary=events.get(gameId);
            ary.add(event);
            events.put(gameId,ary);
            response.status(201);
            response.header("", "events"+event.getId());
            return response;
        }catch (JsonSyntaxException e){
            return "JsonSyntaxException bei post/events";
        }
    }


    private Object deleteEventSubscriptions(Request request, Response response) {
        String gameId = request.queryString();
        events.remove(gameId);
        response.status(200);
        return null;
    }

    private Object getEventSubscriptions(Request request, Response response) {
        response.body(gson.toJson(subsList));
        response.status(200);
        return response;
    }

    private Object getEventDetails(Request request, Response response) {
        String eventId = request.params(":eventId");
        for (String key : events.keySet()){
            for (Event element : events.get(key)){
                if (element.getId().equals(eventId)){
                    response.body(gson.toJson(element));
                    response.status(200);
                    return response;
                }
            }
        }
        response.status(404);
        return null;
    }

    private Object getEvents(Request request, Response response) {
        String gameId = request.queryString();
        ArrayList<Event> ary = new ArrayList<Event>();
        ary = events.get(gameId);
        String json = gson.toJson(ary);
        response.status(200);
        response.body(json);
        return response;
    }
}
