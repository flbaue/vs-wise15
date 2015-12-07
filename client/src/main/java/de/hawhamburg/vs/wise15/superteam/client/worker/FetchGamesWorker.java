package de.hawhamburg.vs.wise15.superteam.client.worker;

import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.callback.CallbackAB;
import de.hawhamburg.vs.wise15.superteam.client.model.GameCollection;
import retrofit.Response;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by florian on 07.12.15.
 */
public class FetchGamesWorker extends SwingWorker<GameCollection, Void> {

    private final GamesAPI gamesAPI;
    private final CallbackAB<GameCollection, Exception> callback;
    private Exception e;


    public FetchGamesWorker(GamesAPI gamesAPI, CallbackAB<GameCollection, Exception> callback) {

        this.gamesAPI = gamesAPI;
        this.callback = callback;
    }

    @Override
    protected GameCollection doInBackground() {
        GameCollection games = null;

        try {
            Response<GameCollection> gamesResponse = gamesAPI.all().execute();

            if (gamesResponse.isSuccess()) {
                games = gamesResponse.body();
            }
        } catch (Exception e) {
            this.e = e;
        }

        return games;
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
