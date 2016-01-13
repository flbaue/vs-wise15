package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.api.ApiFactory;
import de.hawhamburg.vs.wise15.superteam.client.api.BoardsAPI;
import de.hawhamburg.vs.wise15.superteam.client.api.DiceAdapter;
import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.model.*;
import de.hawhamburg.vs.wise15.superteam.client.worker.FetchCurrentPlayerWorker;
import de.hawhamburg.vs.wise15.superteam.client.worker.RollDiceWorker;
import retrofit.Response;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.Objects;


/**
 * Created by florian on 11.01.16.
 */
public class GameFormSimple implements LifeCycle {
    private final Client client;
    private final DiceAdapter diceAdapter;
    private final ApiFactory apiFactory;
    private Game game;
    private Player player;

    private String[] columnNames = {"Spieler", "Feld", "Status"};
    private String[][] data = {{"Tom", "Los", "Aktiv"}, {"Mary", "Los", "Wartet"}};
    private JPanel panel;
    private JButton rollDiceButton;
    private JButton buyStreetButton;
    private JButton nextPlayerButton;
    private JTable gameTable;

    private RollDiceWorker rollDiceWorker;
    private FetchCurrentPlayerWorker fetchCurrentPlayerWorker;
    private BoardsAPI boardsAPI;
    private GamesAPI gamesAPI;

    public GameFormSimple(Client client, DiceAdapter diceAdapter, ApiFactory apiFactory) {

        this.client = client;
        this.diceAdapter = diceAdapter;
        this.apiFactory = apiFactory;
        client.playerServiceController.addCommandListener("TURN", e -> playerTurn());

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        gameTable.setModel(tableModel);
        buyStreetButton.setEnabled(false);
        rollDiceButton.setEnabled(false);
        rollDiceButton.addActionListener(e -> rollDice());
        nextPlayerButton.addActionListener(e -> nextPlayer());


    }

    private void rollDice() {
        rollDiceWorker = new RollDiceWorker(game, diceAdapter, this::dicesRolled);
        rollDiceWorker.execute();
    }

    private void dicesRolled(Roll[] rolls, Exception e) {
        try {
            if (rolls != null) {
                client.showInfoDialog("Würfel 1: " + rolls[0] + "\nWürfel 2: " + rolls[1]);
                BoardState boardState = boardsAPI.setRoll(game.getGameid(), player.getId(), new DoubleRoll(rolls)).execute().body();
                refresh();

            } else {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void nextPlayer() {
        rollDiceButton.setEnabled(false);

    }

    private void playerTurn() {
        System.out.println("turn");
        rollDiceButton.setEnabled(true);
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

        boardsAPI = apiFactory.getBoardsAPI(game.getComponents().getBoard());
        gamesAPI = apiFactory.getGamesAPI(game.getComponents().getGame());

        try {
            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refresh() throws IOException {
        Player currentPlayer = null;
        PlayerCollection playerCollection = null;

        Response<Player> response1 = gamesAPI.currentPlayer(game.getGameid()).execute();
        currentPlayer = response1.body();

        Response<PlayerCollection> response2 = game.getComponents().getGamesAPI().allPlayers(game.getGameid()).execute();
        playerCollection = response2.body();


        Objects.requireNonNull(playerCollection);
        Objects.requireNonNull(currentPlayer);
        int numberOfPlayers = playerCollection.getPlayers().size();


        data = new String[numberOfPlayers][columnNames.length];

        for (int i = 0; i < numberOfPlayers; i++) {
            Player p = playerCollection.getPlayers().get(i);
            data[i][0] = p.getName();
            data[i][1] = boardsAPI.getPlayer(game.getGameid(), p.getId()).execute().body().getPlace().getName();
            data[i][2] = playerCollection.getPlayers().get(i).getId().equals(currentPlayer.getId()) ? "Aktiv" : "Wartet";
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        gameTable.setModel(tableModel);
    }

    private void refresh(BoardState boardState) {

    }
}
