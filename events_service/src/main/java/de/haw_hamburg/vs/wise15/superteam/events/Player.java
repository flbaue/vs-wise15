package de.haw_hamburg.vs.wise15.superteam.events;

public class Player {

    private String playerId;
    private String name;
    private String uri;
    private Place place;
    private int position;
    private boolean ready;
    private transient boolean turn;

    public boolean matchesPlayer(Player p){
        return name.matches(p.getName())
                && uri.matches(p.getUri())
                && place.matchesPlace(p.getPlace());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return playerId.equals(player.playerId);

    }

    @Override
    public int hashCode() {
        return playerId.hashCode();
    }

    public Player(String playerId, String name, String uri, Place place, int position) {
        this.playerId = playerId;
        this.name = name;
        this.uri = uri;
        this.place = place;
        this.position = position;


    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }
}
