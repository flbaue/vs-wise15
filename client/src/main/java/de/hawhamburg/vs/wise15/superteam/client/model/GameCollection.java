package de.hawhamburg.vs.wise15.superteam.client.model;

import java.util.List;

/**
 * Created by florian on 16.11.15.
 */
public class GameCollection {

    List<Game> games;


    public GameCollection() {

    }


    public GameCollection(List<Game> games) {

        this.games = games;
    }


    public List<Game> getGames() {

        return games;
    }
}
