package de.hawhamburg.vs.wise15.superteam.client.model;

/**
 * Created by florian on 16.11.15.
 */
public class Components {
    private String game;
    private String dice;
    private String board;
    private String bank;
    private String broker;
    private String decks;
    private String events;


    public Components() {

    }


    public Components(String game, String dice, String board, String bank, String broker, String decks, String events) {

        this.game = game;
        this.dice = dice;
        this.board = board;
        this.bank = bank;
        this.broker = broker;
        this.decks = decks;
        this.events = events;
    }


    public String getGame() {

        return game;
    }


    public String getDice() {

        return dice;
    }


    public String getBoard() {

        return board;
    }


    public String getBank() {

        return bank;
    }


    public String getBroker() {

        return broker;
    }


    public String getDecks() {

        return decks;
    }


    public String getEvents() {

        return events;
    }
}
