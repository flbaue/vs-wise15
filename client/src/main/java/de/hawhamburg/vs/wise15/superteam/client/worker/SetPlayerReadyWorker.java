package de.hawhamburg.vs.wise15.superteam.client.worker;

import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.callback.CallbackAB;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import retrofit.Response;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by florian on 14.12.15.
 */
public class SetPlayerReadyWorker extends SwingWorker<Boolean, Void> {

    private final GamesAPI gamesAPI;
    private final Game game;
    private final Player player;
    private final CallbackAB<Boolean, Exception> callback;

    public SetPlayerReadyWorker(GamesAPI gamesAPI, Game game, Player player, CallbackAB<Boolean, Exception> callback) {

        this.gamesAPI = gamesAPI;
        this.game = game;
        this.player = player;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        Response<Void> response = gamesAPI.setPlayerReady(game.getGameid(), player.getId()).execute();
        return response.isSuccess();
    }

    @Override
    protected void done() {
        try {
            callback.callback(get(), null);
        } catch (InterruptedException | ExecutionException e) {
            callback.callback(false, e);
        }
    }
}
