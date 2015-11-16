package de.hawhamburg.vs.wise15.superteam.client.api;

import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import retrofit.Call;
import retrofit.http.POST;

/**
 * Created by florian on 16.11.15.
 */
public interface PlayersAPI {

    // TODO rest api?

    @POST("players")
    Call<Player> createPlayer();

}
