package de.haw_hamburg.vs.wise15.superteam.events;

public class Place {

    private String name;

    public Place(String name){
        this.name=name;
    }

    public boolean matchesPlace(Place p){
        return name.matches(p.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
