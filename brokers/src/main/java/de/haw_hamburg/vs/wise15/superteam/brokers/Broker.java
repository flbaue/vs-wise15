package de.haw_hamburg.vs.wise15.superteam.brokers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by masha on 28.11.15.
 */
public class Broker {

    //brokerId == gameId
    private Map<String, ArrayList<Place>> allPlaces = new HashMap<>();
    private Map<String, ArrayList<Estate>> allEstates = new HashMap<>();
    private ArrayList<Game> games = new ArrayList<Game>();


   public void addGame(Game game){
       games.add(game);
   }

    public ArrayList<Place> addPlace(String placeId, String gameId){
        Place place = new Place(placeId);
        ArrayList<Place> value = new ArrayList<>();
        value = allPlaces.get(gameId);
        if (value == null){
             value = new ArrayList<>();
        }
        value.add(place);
        ArrayList<Place> s = allPlaces.put(gameId, value);
        return  s;
    }
    public ArrayList<Estate> addEstate(String gameId, Estate estate){
        ArrayList<Estate> value = new ArrayList<>();
        value = allEstates.get(gameId);
        if (value == null){
            value = new ArrayList<>();
        }
        value.add(estate);
        ArrayList<Estate> s = allEstates.put(gameId, value);
        return  s;
    }


    public Map<String, ArrayList<Place>> getAllPlaces() {
        return allPlaces;
    }

    public void setAllPlaces(Map<String, ArrayList<Place>> allPlaces) {
        this.allPlaces = allPlaces;
    }

    public Map<String, ArrayList<Estate>> getAllEstates() {
        return allEstates;
    }

    public void setAllEstates(Map<String, ArrayList<Estate>> allEstates) {
        this.allEstates = allEstates;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }}
