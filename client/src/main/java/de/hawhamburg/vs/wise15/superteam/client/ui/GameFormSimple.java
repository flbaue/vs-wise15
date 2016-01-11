package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 * Created by florian on 11.01.16.
 */
public class GameFormSimple implements LifeCycle{
    private final Client client;
    private Game game;
    private Player player;

    private String[] columnNames = {"Spieler", "Feld", "Status"};
    private String[][] data = {{"Tom", "Los", "Aktiv"}, {"Mary", "Los", "Wartet"}};
    private JPanel panel;
    private JButton rollDiceButton;
    private JButton buyStreetButton;
    private JButton nextPlayerButton;
    private JTable gameTable;

    public GameFormSimple(Client client) {

        this.client = client;
        client.playerServiceController.addCommandListener("TURN", e -> playerTurn());

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        gameTable.setModel(tableModel);
    }

    private void playerTurn() {
        System.out.println();
    }

    public void setGame(Game game) {

        this.game = game;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void willAppear() {
        DefaultTableModel tableModel = new DefaultTableModel(columnNames,game.getPlayers().getPlayers().size());

        for(Player player : game.getPlayers().getPlayers()) {

        }

    }
}
