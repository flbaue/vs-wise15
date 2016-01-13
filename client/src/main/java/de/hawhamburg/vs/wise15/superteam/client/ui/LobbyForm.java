package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.Utils;
import de.hawhamburg.vs.wise15.superteam.client.api.EventsAdapter;
import de.hawhamburg.vs.wise15.superteam.client.model.*;
import de.hawhamburg.vs.wise15.superteam.client.worker.DeletePlayerWorker;
import de.hawhamburg.vs.wise15.superteam.client.worker.FetchPlayersWorker;
import de.hawhamburg.vs.wise15.superteam.client.worker.SetPlayerReadyWorker;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by florian on 16.11.15.
 */
public class LobbyForm implements LifeCycle {
    private static final Logger log = Utils.getLogger(LobbyForm.class.getName());
    private final Client client;
    private final Components components;
    private final EventsAdapter eventsAdapter;
    private final Timer timer;
    private JPanel panel;
    private JList playerList;
    private JButton readyButton;
    private JButton exitButton;
    private Game game;
    private Player player;
    private DeletePlayerWorker deletePlayerWorker;
    private SetPlayerReadyWorker setPlayerReadyWorker;


    public LobbyForm(Client client, Components components, EventsAdapter eventsAdapter) {
        this.client = client;

        this.components = components;
        this.eventsAdapter = eventsAdapter;

        exitButton.addActionListener(e -> {
            deletePlayerWorker = new DeletePlayerWorker(components, game.getGameid(), player.getId(), this::leaveLobby);
            deletePlayerWorker.execute();
        });

        readyButton.addActionListener(e -> {
            setPlayerReadyWorker = new SetPlayerReadyWorker(components, game, player, this::playerReadyCallback);
            setPlayerReadyWorker.execute();
        });

        timer = new Timer(2000, e -> refresh()); //TODO: game service should send event to player

        client.playerServiceController.addCommandListener("EVENTS", e -> gameStarted(e));
    }

    private void gameStarted(String e) {
        if (e.contains("GAME_START")) {
            timer.stop();
            client.openGameForm(game, player);
        }
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
        FetchPlayersWorker fetchPlayersWorker = new FetchPlayersWorker(components, game, this::playersReceived);
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
        Event event = new Event(null, "GAME_START", null, null, null);
        Subscription subscription = new Subscription(game.getGameid(), client.playerServiceController.getUri() + "/event", event);

        try {
            eventsAdapter.createSubscription(subscription, game.getComponents().getEvents());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
