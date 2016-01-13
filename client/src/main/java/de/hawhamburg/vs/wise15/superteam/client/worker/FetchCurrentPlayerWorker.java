package de.hawhamburg.vs.wise15.superteam.client.worker;

import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.callback.CallbackAB;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import retrofit.Response;

import javax.swing.*;

/**
 * Created by florian on 12.01.16.
 */
public class FetchCurrentPlayerWorker extends SwingWorker<Player, Void> {

    private final Game game;
    private final CallbackAB<Player, Exception> callback;

    public FetchCurrentPlayerWorker(Game game, CallbackAB<Player, Exception> callback) {

        this.game = game;
        this.callback = callback;
    }

    @Override
    protected Player doInBackground() throws Exception {
        GamesAPI gamesAPI = game.getComponents().getGamesAPI();
        Response<Player> response = gamesAPI.currentPlayer(game.getGameid()).execute();
        if (response.isSuccess()) {
            return response.body();
        } else {
            return null;
        }
    }

    @Override
    protected void done() {
        try {
            callback.callback(get(), null);
        } catch (Exception e) {
            callback.callback(null, e);
        }
    }
}
