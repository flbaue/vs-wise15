package de.hawhamburg.vs.wise15.superteam.client.api;

import de.hawhamburg.vs.wise15.superteam.client.model.BoardState;
import de.hawhamburg.vs.wise15.superteam.client.model.DoubleRoll;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by florian on 13.01.16.
 */
public interface BoardsAPI {

    @GET("/boards/{gameId}/players/{playerId}")
    Call<Player> getPlayer(@Path("gameId") String gameId, @Path("playerId") String playerId);


    @POST("/boards/{gameId}/players/{playerId}/roll")
    Call<BoardState> setRoll(@Path("gameId") String gameId, @Path("playerId") String playerId, @Body DoubleRoll rolls);
}
