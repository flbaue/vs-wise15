package de.hawhamburg.vs.wise15.superteam.client.worker;

import de.hawhamburg.vs.wise15.superteam.client.callback.CallbackABC;
import de.hawhamburg.vs.wise15.superteam.client.model.Components;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import retrofit.Response;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by florian on 07.12.15.
 */
public class AddPlayerWorker extends SwingWorker<Boolean, Void> {


    private final Components components;
    private final Player player;
    private final CallbackABC<Game, Player, Exception> callback;
    private Game game;
    private Exception e;

    public AddPlayerWorker(Components components, Game game, Player player, CallbackABC<Game, Player, Exception> callback) {

        this.components = components;
        this.game = game;
        this.player = player;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        try {
            Response<Void> response = components.getGamesAPI().joinPlayer(
                    game.getGameid(),
                    player.getId(),
                    player.getName(),
                    player.getUri()
            ).execute();

            Response<Game> gameResponse = components.getGamesAPI().byId(game.getGameid()).execute();
            this.game.setPlayers(gameResponse.body().getPlayers());

            return response.isSuccess() && gameResponse.isSuccess();

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
