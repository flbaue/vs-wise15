package de.hawhamburg.vs.wise15.superteam.client.model;

/**
 * Created by florian on 16.11.15.
 */
public class Player {

    private String id;
    private String name;
    private String uri;
    private Place place;
    private int position;


    public Player() {

    }


    public Player(String id, String name, String uri, Place place, int position) {

        this.id = id;
        this.name = name;
        this.uri = uri;
        this.place = place;
        this.position = position;
    }


    public String getId() {

        return id;
    }


    public String getName() {

        return name;
    }


    public String getUri() {

        return uri;
    }


    public Place getPlace() {

        return place;
    }


    public int getPosition() {

        return position;
    }

    @Override
    public String toString() {
        return name;
    }
}
