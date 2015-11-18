package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.api.PlayersAPI;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.GameCollection;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import de.hawhamburg.vs.wise15.superteam.client.model.PlayerCollection;
import retrofit.Response;
import retrofit.Retrofit;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by florian on 16.11.15.
 */
public class SearchForm {

    private final GamesAPI gamesAPI;
    private final PlayersAPI playersAPI;

    private JPanel panel;
    private JList<Game> gameList;
    private JList playerList;
    private JButton backButton;
    private JTextField playerNameTxt;
    private JButton enterGameButton;

    private FetchGamesWorker fetchGamesWorker;
    private FetchPlayersWorker fetchPlayersWorkerWorker;


    public SearchForm(Client client, Retrofit retrofit) {

        gamesAPI = retrofit.create(GamesAPI.class);
        playersAPI = retrofit.create(PlayersAPI.class);

        refresh();

        ListSelectionModel selectionModel = gameList.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            fetchPlayersWorkerWorker = new FetchPlayersWorker();
            fetchPlayersWorkerWorker.execute();
        });

        backButton.addActionListener(e -> client.openStartForm());
        enterGameButton.addActionListener(e -> {
            Game game = gameList.getSelectedValue();
            if (joinPlayer(game)) {
                client.openLobbyForm(game);
            }
        });
    }


    public void refresh() {

        fetchGames();
    }


    public JPanel getPanel() {

        return panel;
    }


    private boolean joinPlayer(Game game) {


        Player player;

        try {
            Response<Player> playerResponse = playersAPI.createPlayer().execute();
            if (playerResponse.isSuccess()) {
                player = playerResponse.body();
            } else {
                errorPlayerNotCreated(new IOException(playerResponse.message()));
                return false;
            }
        } catch (IOException e) {
            errorPlayerNotCreated(e);
            return false;
        }

        try {
            Response<String> joinResponse = gamesAPI.joinPlayer(
                    game.getGameid(),
                    player.getId(),
                    playerNameTxt.getText(),
                    player.getUri()).execute();

            if (joinResponse.isSuccess()) {
                return true;
            } else {
                errorGameNotJoined(new IOException(joinResponse.message()));
                return false;
            }
        } catch (IOException e) {
            errorGameNotJoined(e);
            return false;
        }
    }


    private void fetchGames() {

        fetchGamesWorker = new FetchGamesWorker();
        fetchGamesWorker.execute();
    }


    private void errorGameNotJoined(IOException e) {

        e.printStackTrace();

    }


    private void errorPlayerNotCreated(IOException e) {

        e.printStackTrace();

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


    private class FetchPlayersWorker extends SwingWorker<Void, Void> {

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
                PlayerCollection players = game.getPlayers();

                if (players != null && players.getPlayers() != null) {
                    for (Player player : players.getPlayers()) {
                        playerListModel.addElement(player.getName());
                    }
                }
            } else {
                errorFetchingGameDetails(e);
            }
            playerList.setModel(playerListModel);
        }
    }
}
