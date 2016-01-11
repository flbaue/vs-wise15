package de.hawhamburg.vs.wise15.superteam.client.ui;

import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.api.PlayersAPI;
import de.hawhamburg.vs.wise15.superteam.client.model.*;
import de.hawhamburg.vs.wise15.superteam.client.worker.FetchGamesWorker;
import de.hawhamburg.vs.wise15.superteam.client.worker.FetchPlayersWorker;
import retrofit.Response;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by florian on 16.11.15.
 */
public class SearchForm implements LifeCycle{

    private final Client client;
    private final GamesAPI gamesAPI;
    private final PlayersAPI playersAPI;

    private JPanel panel;
    private JList<Game> gameList;
    private JList<Player> playerList;
    private JButton backButton;
    private JTextField playerNameTxt;
    private JButton enterGameButton;

    private Player player;

    public SearchForm(Client client, GamesAPI gamesAPI, PlayersAPI playersAPI) {
        this.client = client;

        this.gamesAPI = gamesAPI;
        this.playersAPI = playersAPI;

        ListSelectionModel selectionModel = gameList.getSelectionModel();
        selectionModel.addListSelectionListener(event -> {
            Game selectedGame = gameList.getSelectedValue();
            if (selectedGame != null) {
                FetchPlayersWorker fetchPlayersWorkerWorker = new FetchPlayersWorker(
                        this.gamesAPI,
                        selectedGame,
                        this::playersReceived);

                fetchPlayersWorkerWorker.execute();
            } else {
                playerList.setModel(new DefaultListModel<>());
            }
        });

        backButton.addActionListener(e -> client.openStartForm());
        enterGameButton.addActionListener(e -> {
            Game game = gameList.getSelectedValue();
            if (joinPlayer(game)) {
                client.components.set(game.getComponents());
                client.openLobbyForm(game, player);
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

        String id = String.valueOf(Math.round(Math.random() * 1000));
        player = new Player(id, playerNameTxt.getText(), client.playerServiceController.getUri(), new Place(""), 42, false);


        try {
            Response<Void> joinResponse = gamesAPI.joinPlayer(
                    game.getGameid(),
                    player.getId(),
                    playerNameTxt.getText(),
                    player.getUri()).execute();

            if (joinResponse.isSuccess()) {
                this.player = player;
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

        FetchGamesWorker fetchGamesWorker = new FetchGamesWorker(gamesAPI, this::gamesReceived);
        fetchGamesWorker.execute();
    }


    private void gamesReceived(GameCollection gameCollection, Exception exception) {
        if (gameCollection == null) {
            errorFetchingGames(exception);
            return;
        }
        DefaultListModel<Game> gameListModel = new DefaultListModel<>();
        gameCollection.getGames().forEach(gameListModel::addElement);
        gameList.setModel(gameListModel);
    }


    private void playersReceived(PlayerCollection playerCollection, Exception exception) {
        if (playerCollection == null) {
            errorFetchingGameDetails(exception);
            return;
        }
        DefaultListModel<Player> playersListModel = new DefaultListModel<>();
        playerCollection.getPlayers().forEach(playersListModel::addElement);
        playerList.setModel(playersListModel);
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

    @Override
    public void willAppear() {
        refresh();
    }
}
