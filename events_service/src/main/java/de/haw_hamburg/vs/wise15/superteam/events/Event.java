package de.haw_hamburg.vs.wise15.superteam.events;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Event {

    private transient String id;
    private String type;
    private String name;
    private String reason;
    private String resource;
    private Player player;
    private transient Gson gson = new Gson();

    public Event(String type, String name, String reason, String resource, String player) {
        this.type = type;
        this.name = name;
        this.reason = reason;
        this.resource = resource;
        try {
            this.player = gson.fromJson(player, Player.class);
        } catch (JsonSyntaxException e) {

        }
    }

    public Event() {

    }

    public boolean matchesEvent(Subscription s) {

        boolean result = false;
        Event subscriptionMask = s.getEvent();

        if (subscriptionMask != null) {

            if (subscriptionMask.type != null && type != null && type.matches(subscriptionMask.type)) {
                result = true;
            } else if (subscriptionMask.name != null && name != null && name.matches(subscriptionMask.name)) {
                result = true;
            } else if (subscriptionMask.reason != null && reason != null && reason.matches(subscriptionMask.reason)) {
                result = true;
            } else if (subscriptionMask.resource != null && resource != null && resource.matches(subscriptionMask.resource)) {
                result = true;
            } else {
                result = subscriptionMask.player != null && player != null && player.matchesPlayer(subscriptionMask.player);
            }
        }

        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
