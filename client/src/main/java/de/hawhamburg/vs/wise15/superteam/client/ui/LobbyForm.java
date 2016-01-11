package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.Utils;
import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import de.hawhamburg.vs.wise15.superteam.client.model.PlayerCollection;
import de.hawhamburg.vs.wise15.superteam.client.worker.DeletePlayerWorker;
import de.hawhamburg.vs.wise15.superteam.client.worker.FetchPlayersWorker;
import de.hawhamburg.vs.wise15.superteam.client.worker.SetPlayerReadyWorker;

import javax.swing.*;
import java.util.logging.Logger;

/**
 * Created by florian on 16.11.15.
 */
public class LobbyForm implements LifeCycle {
    private static final Logger log = Utils.getLogger(LobbyForm.class.getName());
    private final Client client;
    private final GamesAPI gamesAPI;
    private final Timer timer;
    private JPanel panel;
    private JList playerList;
    private JButton readyButton;
    private JButton exitButton;
    private Game game;
    private Player player;
    private DeletePlayerWorker deletePlayerWorker;
    private SetPlayerReadyWorker setPlayerReadyWorker;


    public LobbyForm(Client client, GamesAPI gamesAPI) {
        this.client = client;

        this.gamesAPI = gamesAPI;

        exitButton.addActionListener(e -> {
            deletePlayerWorker = new DeletePlayerWorker(gamesAPI, game.getGameid(), player.getId(), this::leaveLobby);
            deletePlayerWorker.execute();
        });

        readyButton.addActionListener(e -> {
            setPlayerReadyWorker = new SetPlayerReadyWorker(gamesAPI, game, player, this::playerReadyCallback);
            setPlayerReadyWorker.execute();
        });

        timer = new Timer(2000, e -> refresh()); //TODO: game service should send event to player

        client.playerServiceController.addCommandListener("GAME_STARTED", e -> gameStarted());
    }

    private void gameStarted() {
        timer.stop();
        client.openGameForm(game, player);
    }

    private void playerReadyCallback(Boolean playerReady, Exception e) {
        if (playerReady == false) {
            showError(e);
            return;
        }

        player.setReady(playerReady);
        refresh();

    }

    private void refresh() {
        FetchPlayersWorker fetchPlayersWorker = new FetchPlayersWorker(gamesAPI, game, this::playersReceived);
        fetchPlayersWorker.execute();
    }

    private void playersReceived(PlayerCollection playerCollection, Exception e) {
        if (e != null) {
            showError(e);
            return;
        }
        DefaultListModel<Player> playerListModel = new DefaultListModel<>();
        playerCollection.getPlayers().forEach(playerListModel::addElement);
        playerList.setModel(playerListModel);
    }

    private void showError(Exception e) {
        e.printStackTrace(); //TODO
    }

    private void leaveLobby() {
        timer.stop();
        client.openStartForm();
    }

    public JPanel getPanel() {

        return panel;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void willAppear() {
        refresh();
        timer.start();
    }
}
