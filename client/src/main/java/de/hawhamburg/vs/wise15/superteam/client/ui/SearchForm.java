package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.GameCollection;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import retrofit.Response;
import retrofit.Retrofit;

import javax.swing.*;

/**
 * Created by florian on 16.11.15.
 */
public class SearchForm {

    private final GamesAPI gamesAPI;

    private JPanel panel;
    private JList<Game> gameList;
    private JList playerList;
    private JButton backButton;
    private JTextField textField1;
    private JButton enterGameButton;
    private FetchGamesWorker fetchGamesWorker;
    private FetchGameDetailsWorker fetchGameDetailsWorker;


    public SearchForm(Client client, Retrofit retrofit) {

        gamesAPI = retrofit.create(GamesAPI.class);

        fetchGames();

        ListSelectionModel selectionModel = gameList.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            fetchGameDetailsWorker = new FetchGameDetailsWorker();
            fetchGameDetailsWorker.execute();
        });

        backButton.addActionListener(e -> client.openStartForm());
        enterGameButton.addActionListener(e -> client.openLobbyForm(gameList.getSelectedValue()));
    }


    private void fetchGames() {

        fetchGamesWorker = new FetchGamesWorker();
        fetchGamesWorker.execute();
    }


    public JPanel getPanel() {

        return panel;
    }


    private void errorFetchingGames(Exception e) {
        e.printStackTrace();
        //TODO
    }


    private void errorFetchingGameDetails(Exception e) {
        e.printStackTrace();
        //TODO
    }


    private class FetchGamesWorker extends SwingWorker<Void, Void> {

        private GameCollection games;
        private Exception e;


        @Override
        protected Void doInBackground() {

            try {
                Response<GameCollection> gamesResponse = gamesAPI.all().execute();

                if (gamesResponse.isSuccess()) {
                    games = gamesResponse.body();
                }
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }


        @Override
        protected void done() {

            DefaultListModel<Game> gameListModel = new DefaultListModel<>();
            if (games != null) {
                games.getGames().forEach(gameListModel::addElement);
            } else {
                errorFetchingGames(e);
            }
            gameList.setModel(gameListModel);
        }
    }

    private class FetchGameDetailsWorker extends SwingWorker<Void, Void> {

        private Game game;
        private Exception e;


        @Override
        protected Void doInBackground() {

            try {
                Game selection = gameList.getSelectedValue();
                Response<Game> response = gamesAPI.byId(selection.getGameid()).execute();

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

            DefaultListModel<String> playerListModel = new DefaultListModel<>();
            if (game != null) {
                for (Player player : game.getPlayers()) {
                    playerListModel.addElement(player.getName());
                }
            } else {
                errorFetchingGameDetails(e);
            }
            playerList.setModel(playerListModel);
        }
    }
}
