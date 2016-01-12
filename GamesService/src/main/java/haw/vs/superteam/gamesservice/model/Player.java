package haw.vs.superteam.gamesservice.model;

import haw.vs.superteam.gamesservice.api.PlayerAPI;

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
    private transient PlayerAPI api;

    public Player() {

    }


    public Player(String id, String name, String uri, Place place, int position) {

        this.id = id;
        this.name = name;
        this.uri = uri;
        this.place = place;
        this.position = position;
    }

    public Player(String playerId, String playerName, String playerURI) {

        id = playerId;
        name = playerName;
        uri = playerURI;
        place = null;
        position = -1;
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

    public void setPlace(Place place) {
        this.place = place;
    }

    public int getPosition() {

        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return !(id != null ? !id.equals(player.id) : player.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public PlayerAPI getAPI() {
        return api;
    }

    public void setAPI(PlayerAPI api) {
        this.api = api;
    }
}

