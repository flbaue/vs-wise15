package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;

import javax.swing.*;

/**
 * Created by florian on 16.11.15.
 */
public class LobbyForm {
    private JPanel panel;
    private JList list1;
    private JButton readyButton;
    private JButton exitButton;


    public LobbyForm(Client client) {


        exitButton.addActionListener(e -> client.openStartForm());
    }


    public JPanel getPanel() {

        return panel;
    }
}
