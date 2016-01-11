package haw.vs.superteam.gamesservice.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by florian on 11.01.16.
 */
public class Event {
    private transient String id;
    private String type;
    private String name;
    private String reason;
    private String resource;
    private Player player;


    public Event() {

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
