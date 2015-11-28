package de.haw_hamburg.vs.wise15.superteam.events;


public class Event {

    private String type;
    private String name;
    private String reason;
    private String resource;
    private Player player;

    public Event(String type, String name, String reason, String resource, Player player){
        this.type = type;
        this.name = name;
        this.reason = reason;
        this.resource=resource;
        this.player = player;
    }
    public Event(){

    }
}
