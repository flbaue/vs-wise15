package de.hawhamburg.vs.wise15.superteam.client.worker;

import de.hawhamburg.vs.wise15.superteam.client.Utils;
import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.callback.Callback;
import de.hawhamburg.vs.wise15.superteam.client.model.Components;
import retrofit.Response;

import javax.swing.*;
import java.util.logging.Logger;

/**
 * Created by florian on 07.12.15.
 */
public class DeletePlayerWorker extends SwingWorker<Void, Void> {

    private static final Logger log = Utils.getLogger(DeletePlayerWorker.class.getName());

    private final Components components;
    private final String gameId;
    private final String playerId;
    private final Callback callback;

    public DeletePlayerWorker(Components components, String gameId, String playerId, Callback callback) {

        this.components = components;
        this.gameId = gameId;
        this.playerId = playerId;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground() throws Exception {
        Response<Void> response = components.getGamesAPI().deletePlayer(gameId, playerId).execute();

        if (!response.isSuccess()) {
            log.warning("Deleting player " + playerId + "from game " + gameId + " failed!");
        }

        return null;
    }

    @Override
    protected void done() {
        callback.callback();
    }
}
