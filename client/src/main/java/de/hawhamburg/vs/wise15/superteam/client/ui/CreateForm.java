package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.Place;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import de.hawhamburg.vs.wise15.superteam.client.worker.AddPlayerWorker;
import de.hawhamburg.vs.wise15.superteam.client.worker.CreateGameWorker;

import javax.swing.*;

/**
 * Created by florian on 16.11.15.
 */
public class CreateForm implements LifeCycle{
    private final Client client;
    private final GamesAPI gamesAPI;
    private JPanel panel;
    private JTextField textField1;
    private JButton createGameButton;
    private JButton backButton;


    public CreateForm(Client client, GamesAPI gamesAPI) {

        this.gamesAPI = gamesAPI;

        this.client = client;

        backButton.addActionListener(e -> client.openStartForm());
        createGameButton.addActionListener(e -> {
            CreateGameWorker createGameWorker = new CreateGameWorker(gamesAPI, this::gameCreated, client.components);
            createGameWorker.execute();
        });
    }

    public JPanel getPanel() {

        return panel;
    }

    private void gameCreated(Game game, Exception exception) {
        if (game == null) {
            gameNotCreated(exception);
            return;
        }


        //TODO create player
        String id = String.valueOf(Math.round(Math.random() * 1000));
        Player player = new Player(id, textField1.getText(), client.playerServiceController.getUri(), new Place(""), 42, false);

        AddPlayerWorker addPlayerWorker = new AddPlayerWorker(gamesAPI, game, player, this::playerAdded);
        addPlayerWorker.execute();
    }

    private void playerAdded(Game game, Player player, Exception e) {
        if (e != null) {
            playerNotAdded(e);
            return;
        }

        client.openLobbyForm(game, player);
    }


    private void playerNotAdded(Exception e) {
        // TODO Show Error
        e.printStackTrace();
    }


    private void gameNotCreated(Exception e) {
        // TODO Show Error
        e.printStackTrace();
    }


    @Override
    public void willAppear() {

    }
}
