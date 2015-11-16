package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.model.Game;

import java.awt.*;

/**
 * Created by florian on 16.11.15.
 */
public class GameForm {
    private Game game;
    private Container panel;


    public void setGame(Game game) {

        this.game = game;
    }


    public Container getPanel() {

        return panel;
    }
}
