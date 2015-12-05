package de.haw_hamburg.vs.wise15.superteam.events;

/**
 * Created by masha on 05.12.15.
 */
public class Subscription {


    private String id;
    private String gameId;
    private String uri;
    private Event event;

    public Subscription(String gameId, String uri, Event event) {
        this.gameId = gameId;
        this.uri = uri;
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
