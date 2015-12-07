package de.hawhamburg.vs.wise15.superteam.client.worker;

import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.callback.CallbackAB;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.PlayerCollection;
import retrofit.Response;

import javax.swing.*;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by florian on 07.12.15.
 */
public class FetchPlayersWorker extends SwingWorker<PlayerCollection, Void> {

    private final GamesAPI gamesAPI;
    private final Game game;
    private final CallbackAB<PlayerCollection, Exception> callback;
    private Exception e;

    public FetchPlayersWorker(GamesAPI gamesAPI, Game game, CallbackAB<PlayerCollection, Exception> callback) {
        Objects.requireNonNull(gamesAPI);
        Objects.requireNonNull(game);
        Objects.requireNonNull(callback);

        this.gamesAPI = gamesAPI;
        this.game = game;
        this.callback = callback;
    }


    @Override
    protected PlayerCollection doInBackground() {

        Game game = null;

        try {
            Response<Game> response = gamesAPI.byId(this.game.getGameid()).execute();

            if (response.isSuccess()) {
                game = response.body();
            }
        } catch (Exception e) {
            this.e = e;
        }

        if (game != null) {
            return game.getPlayers();
        }

        return null;
    }


    @Override
    protected void done() {
        try {
            callback.callback(get(), e);
        } catch (InterruptedException | ExecutionException e) {
            callback.callback(null, e);
        }
    }
}
