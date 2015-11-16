package de.hawhamburg.vs.wise15.superteam.client.ui;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import de.hawhamburg.vs.wise15.superteam.client.Client;
import de.hawhamburg.vs.wise15.superteam.client.Constants;
import de.hawhamburg.vs.wise15.superteam.client.ServiceDirectory;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;

import javax.swing.*;

/**
 * Created by florian on 16.11.15.
 */
public class CreateForm {
    private final Client client;
    private final Gson gson = new Gson();
    private final CreateGameWorker createGameWorker = new CreateGameWorker();
    private JPanel panel;
    private JTextField textField1;
    private JButton createGameButton;
    private JButton backButton;
    private OkHttpClient httpClient = new OkHttpClient();


    public CreateForm(Client client) {

        this.client = client;

        backButton.addActionListener(e -> client.openStartForm());
        createGameButton.addActionListener(e -> createGameWorker.execute());
    }


    private void gameNotCreated() {
        // TODO Show Error
    }


    public JPanel getPanel() {

        return panel;
    }


    private class CreateGameWorker extends SwingWorker<Void, Void> {

        private Game game;


        @Override
        protected Void doInBackground() throws Exception {

            RequestBody requestBody = RequestBody.create(Constants.JSON, "");
            Request request = new Request.Builder()
                    .url(Constants.SERVICE_DIRECTORY_URL + "/games")
                    .post(requestBody)
                    .build();
            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                game = gson.fromJson(response.body().charStream(), Game.class);
            }

            return null;
        }


        @Override
        protected void done() {

            if (game != null) {
                //TODO add user to game
                client.openLobbyForm(game);
            } else {
                gameNotCreated();
            }
        }
    }
}
