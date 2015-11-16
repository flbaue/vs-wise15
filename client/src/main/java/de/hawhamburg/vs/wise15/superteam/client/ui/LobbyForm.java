package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;

import javax.swing.*;

/**
 * Created by florian on 16.11.15.
 */
public class LobbyForm {
    private JPanel panel;
    private JList list1;
    private JButton readyButton;
    private JButton exitButton;
    private Game game;


    public LobbyForm(Client client, Game game) {


        exitButton.addActionListener(e -> client.openStartForm());
    }


    public LobbyForm(Client client) {


    }


    public JPanel getPanel() {

        return panel;
    }


    public void setGame(Game game) {

        this.game = game;
    }
}
