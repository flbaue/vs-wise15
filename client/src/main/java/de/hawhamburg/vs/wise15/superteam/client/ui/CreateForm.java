package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import retrofit.Response;
import retrofit.Retrofit;

import javax.swing.*;

/**
 * Created by florian on 16.11.15.
 */
public class CreateForm {
    private final Client client;
    private final GamesAPI gamesAPI;
    private CreateGameWorker createGameWorker;
    private JPanel panel;
    private JTextField textField1;
    private JButton createGameButton;
    private JButton backButton;


    public CreateForm(Client client, Retrofit retrofit) {

        gamesAPI = retrofit.create(GamesAPI.class);

        this.client = client;

        backButton.addActionListener(e -> client.openStartForm());
        createGameButton.addActionListener(e -> {
            createGameWorker = new CreateGameWorker();
            createGameWorker.execute();
        });
    }


    private void gameNotCreated(Exception e) {
        // TODO Show Error
        e.printStackTrace();
    }


    public JPanel getPanel() {

        return panel;
    }


    private class CreateGameWorker extends SwingWorker<Void, Void> {

        private Game game;
        private Exception e;


        @Override
        protected Void doInBackground() {

            try {
                Response<Game> response = gamesAPI.createGame(new Game()).execute();
                if (response.isSuccess()) {
                    game = response.body();
                }
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }


        @Override
        protected void done() {

            if (game != null) {
                //TODO add user to game
                client.openLobbyForm(game);
            } else {
                gameNotCreated(e);
            }
        }
    }
}
