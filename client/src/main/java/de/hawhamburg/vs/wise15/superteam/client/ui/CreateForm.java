package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;

import javax.swing.*;

/**
 * Created by florian on 16.11.15.
 */
public class CreateForm {
    private final Client client;
    private JPanel panel;
    private JTextField textField1;
    private JButton createGameButton;
    private JButton backButton;


    public CreateForm(Client client) {

        this.client = client;


        backButton.addActionListener(e -> client.openStartForm());
        createGameButton.addActionListener(e -> client.openLobbyForm());
    }


    public JPanel getPanel() {

        return panel;
    }
}
