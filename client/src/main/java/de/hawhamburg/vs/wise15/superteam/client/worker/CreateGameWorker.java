package de.hawhamburg.vs.wise15.superteam.client.worker;

import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.callback.CallbackAB;
import de.hawhamburg.vs.wise15.superteam.client.model.Components;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import retrofit.Response;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by florian on 07.12.15.
 */
public class CreateGameWorker extends SwingWorker<Game, Void> {

    private final GamesAPI gamesAPI;
    private final CallbackAB<Game, Exception> callback;
    private final Components components;
    private Exception e;

    public CreateGameWorker(GamesAPI gamesAPI, CallbackAB<Game, Exception> callback, Components components) {
        this.gamesAPI = gamesAPI;
        this.callback = callback;
        this.components = components;
    }


    @Override
    protected Game doInBackground() {
        Game game = null;
        try {
            Response<Game> response = gamesAPI.createGame(new Game(components)).execute();
            if (response.isSuccess()) {
                game = response.body();
            }
        } catch (Exception e) {
            this.e = e;
        }

        return game;
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
