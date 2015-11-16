package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;

import javax.swing.*;

/**
 * Created by florian on 16.11.15.
 */
public class StartForm {

    private final Client client;
    private JPanel panel;
    private JLabel label;
    private JButton searchGameButton;
    private JButton createGameButton;


    public StartForm(Client client) {

        this.client = client;

        searchGameButton.addActionListener(e -> client.openSearchForm());
        createGameButton.addActionListener(e -> client.openCreateForm());
    }


    public JPanel getPanel() {

        return panel;
    }
}
