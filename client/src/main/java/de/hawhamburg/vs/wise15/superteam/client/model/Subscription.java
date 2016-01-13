package de.hawhamburg.vs.wise15.superteam.client.model;

/**
 * Created by florian on 12.01.16.
 */
public class Subscription {
    private String gameId;
    private String uri;
    private Event event;

    public Subscription(String gameId, String uri, Event event) {
        this.gameId = gameId;
        this.uri = uri;
        this.event = event;
    }
}
