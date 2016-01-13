package de.hawhamburg.vs.wise15.superteam.client.model;

/**
 * Created by florian on 13.01.16.
 */
public class BoardState {

    public Player player;
    public Board board;

    public BoardState(Player player, Board board) {
        this.player = player;
        this.board = board;
    }
}
