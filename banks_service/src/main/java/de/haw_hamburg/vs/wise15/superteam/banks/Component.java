package de.haw_hamburg.vs.wise15.superteam.banks;

/**
 * Created by masha on 16.11.15.
 */
public class Component {
    private String game;
    private String dice;
    private String board;
    private String bank;
    private String broker;
    private String decks;
    private String events;

    public Component(){

    }

    public Component(String events, String game, String dice, String board, String bank, String broker, String decks) {
        this.events = events;
        this.game = game;
        this.dice = dice;
        this.board = board;
        this.bank = bank;
        this.broker = broker;
        this.decks = decks;
    }
}
