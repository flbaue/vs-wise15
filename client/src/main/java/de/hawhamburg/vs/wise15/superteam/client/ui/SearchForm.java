package de.hawhamburg.vs.wise15.superteam.client.ui;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.Constants;
import de.hawhamburg.vs.wise15.superteam.client.ServiceDirectory;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.GameCollection;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by florian on 16.11.15.
 */
public class SearchForm {
    private final Client client;
    private final OkHttpClient httpClient;
    private final Gson gson = new Gson();

    private JPanel panel;
    private JList<Game> gameList;
    private JList playerList;
    private JButton backButton;
    private JTextField textField1;
    private JButton enterGameButton;
    private FetchGamesWorker fetchGamesWorker = new FetchGamesWorker();
    private FetchGameDetailsWorker fetchGameDetailsWorker = new FetchGameDetailsWorker();


    public SearchForm(Client client, OkHttpClient httpClient) {

        this.client = client;
        this.httpClient = httpClient;

        fetchGames();


        System.out.println("Test: " + gson.toJson(new ArrayList<String>()));

        ListSelectionModel selectionModel = gameList.getSelectionModel();
        selectionModel.addListSelectionListener(e -> fetchGameDetailsWorker.execute());

        backButton.addActionListener(e -> this.client.openStartForm());
        enterGameButton.addActionListener(e -> this.client.openLobbyForm(gameList.getSelectedValue()));
    }


    private void fetchGames() {

        fetchGamesWorker.execute();
    }


    public JPanel getPanel() {

        return panel;
    }


    private class FetchGamesWorker extends SwingWorker<Void, Void> {

        GameCollection games;


        @Override
        protected Void doInBackground() throws Exception {

            Request request = new Request.Builder()
                    .url(Constants.SERVICE_DIRECTORY_URL + "/games")
                    .get()
                    .build();

            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                String body = response.body().string();
                System.out.printf(body);
                try {
                    games = gson.fromJson(body, GameCollection.class);
                } catch (JsonSyntaxException e) {
                    System.out.println(e.getMessage());
                }

            }

            return null;
        }


        @Override
        protected void done() {

            DefaultListModel<Game> gameListModel = new DefaultListModel<>();
            if (games != null) {
                games.getGames().forEach(gameListModel::addElement);
            }
            gameList.setModel(gameListModel);
        }
    }

    private class FetchGameDetailsWorker extends SwingWorker<Void, Void> {

        Game game;


        @Override
        protected Void doInBackground() throws Exception {

            Game selection = gameList.getSelectedValue();

            Request request = new Request.Builder()
                    .url(Constants.SERVICE_DIRECTORY_URL + "/games/" + selection.getGameid())
                    .get()
                    .build();

            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                game = gson.fromJson(response.body().charStream(), Game.class);
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
            }
            playerList.setModel(playerListModel);
        }
    }
}
