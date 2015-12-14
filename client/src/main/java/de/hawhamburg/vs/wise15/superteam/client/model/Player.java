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
    private boolean ready;

    public Player() {

    }


    public Player(String id, String name, String uri, Place place, int position, boolean ready) {

        this.id = id;
        this.name = name;
        this.uri = uri;
        this.place = place;
        this.position = position;
        this.ready = ready;
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

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        String r = ready ? "...is ready" : "";
        return name + r;
    }
}
