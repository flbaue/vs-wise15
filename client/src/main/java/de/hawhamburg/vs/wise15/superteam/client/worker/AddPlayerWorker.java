package de.hawhamburg.vs.wise15.superteam.client.worker;

import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.callback.CallbackABC;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import retrofit.Response;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by florian on 07.12.15.
 */
public class AddPlayerWorker extends SwingWorker<Boolean, Void> {


    private final GamesAPI gamesAPI;
    private final Game game;
    private final Player player;
    private final CallbackABC<Game, Player, Exception> callback;
    private Exception e;

    public AddPlayerWorker(GamesAPI gamesAPI, Game game, Player player, CallbackABC<Game, Player, Exception> callback) {

        this.gamesAPI = gamesAPI;
        this.game = game;
        this.player = player;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        try {
            Response<Void> response = gamesAPI.joinPlayer(
                    game.getGameid(),
                    player.getId(),
                    player.getName(),
                    player.getUri()
            ).execute();

            return response.isSuccess();

        } catch (Exception e) {
            this.e = e;
        }

        return false;
    }

    @Override
    protected void done() {
        try {
            if (get()) {
                callback.callback(game, player, e);
            } else {
                callback.callback(game, player, new Exception("Player could not be added to game"));
            }
        } catch (InterruptedException | ExecutionException e) {
            callback.callback(game, player, e);
        }
    }
}
