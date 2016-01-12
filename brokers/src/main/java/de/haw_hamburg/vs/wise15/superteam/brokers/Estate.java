package de.haw_hamburg.vs.wise15.superteam.brokers;

import java.util.ArrayList;

/**
 * Created by masha on 05.12.15.
 */

//estate = place, der schon gekauft wurde
public class Estate {

    private Place place;
    private String owner;
    private Integer value;
    private ArrayList<Integer> rent = new ArrayList<Integer>();
    private ArrayList<Integer> cost = new ArrayList<Integer>();
    private Integer houses;
    private Boolean hypothek;

    public Place getPlace() {
        return place;
    }

    public Boolean getHypothek() {
        return hypothek;
    }

    public void setHypothek(Boolean hypothek) {
        this.hypothek = hypothek;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getRent() {
        return rent.get(houses);
    }

    public void setRent(ArrayList<Integer> rent) {
        this.rent = rent;
    }

    public Integer getCost() {
        return cost.get(houses);
    }

    public void setCost(ArrayList<Integer> cost) {
        this.cost = cost;
    }

    public Integer getHouses() {
        return houses;
    }

    public void setHouses(Integer houses) {
        this.houses = houses;
    }
}
