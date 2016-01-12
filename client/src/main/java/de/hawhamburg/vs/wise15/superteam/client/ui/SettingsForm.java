package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.model.Components;

import javax.swing.*;

/**
 * Created by florian on 10.01.16.
 */
public class SettingsForm implements LifeCycle {
    private final Client client;
    private final Components components;
    private JPanel panel;
    private JTextField playerServiceField;
    private JTextField gamesServiceField;
    private JTextField boardsServiceField;
    private JTextField banksServiceField;
    private JTextField brokerServiceField;
    private JTextField eventsServiceField;
    private JTextField decksServiceField;
    private JTextField diceServiceField;
    private JButton saveButton;
    private JButton backButton;


    public SettingsForm(Client client, Components components) {
        this.client = client;
        this.components = components;
        saveButton.addActionListener(e -> this.saveButtonClicked());
        backButton.addActionListener(e -> this.backButtonClicked());
    }

    private void backButtonClicked() {
        client.openStartForm();
    }

    private void saveButtonClicked() {
        components.setPlayer(playerServiceField.getText());
        components.setGame(gamesServiceField.getText());
        components.setBoard(boardsServiceField.getText());
        components.setBank(banksServiceField.getText());
        components.setBroker(brokerServiceField.getText());
        components.setEvents(eventsServiceField.getText());
        components.setDecks(decksServiceField.getText());
        components.setDice(diceServiceField.getText());
    }


    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void willAppear() {
        playerServiceField.setText(components.getPlayer());
        gamesServiceField.setText(components.getGame());
        boardsServiceField.setText(components.getBoard());
        banksServiceField.setText(components.getBank());
        brokerServiceField.setText(components.getBroker());
        eventsServiceField.setText(components.getEvents());
        decksServiceField.setText(components.getDecks());
        diceServiceField.setText(components.getDice());
    }
}
