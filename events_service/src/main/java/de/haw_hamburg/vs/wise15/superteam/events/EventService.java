package de.haw_hamburg.vs.wise15.superteam.events;

import spark.Request;
import spark.Response;

import static spark.Spark.*;

/**
 * Created by masha on 28.11.15.
 */
public class EventService {


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
        post("/events/subscriptions", this::getEventSubscriptions);

        //removes the subscription from the service
        delete("/events/subscriptions/subscriptions/{subscription}", this::deleteEventSubscriptions);

        //alle Interessenten benachrichtigt werden über neue Events
        //post (uri of the subscroption)
    }

    private Object createEvents(Request request, Response response) {
        return null;
    }

    private Object deleteEventSubscriptions(Request request, Response response) {
        return null;
    }

    private Object getEventSubscriptions(Request request, Response response) {
        return null;
    }

    private Object getEventDetails(Request request, Response response) {
        return null;
    }

    private Object getEvents(Request request, Response response) {
        return null;
    }
}
