package de.hawhamburg.vs.wise15.superteam.client.api;

import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.GameCollection;
import retrofit.Call;
import retrofit.http.*;

/**
 * Created by florian on 16.11.15.
 */
public interface GamesAPI {

    @POST("games")
    Call<Game> createGame();

    @GET("games")
    Call<GameCollection> all();

    @GET("games/{gameId}")
    Call<Game> byId(@Path("gameId") String gameId);

    @PUT("games/{gameId}/players/{playerId}")
    Call<String> joinPlayer(@Path("gameId") String gameId, @Path("playerId") String playerId,
                            @Query("name") String name, @Query("uri") String playerURI);

    @DELETE("games/{gameId}/players/{playerId}")
    Call<String> deletePlayer(@Path("gameId") String gameId, @Path("playerId") String playerId);

    @PUT("games/{gameId}/players/{playerId}/ready")
    Call<String> setPlayerReady(@Path("gameId") String gameId, @Path("playerId") String playerId);

    @GET("games/{gameId}/players/{playerId}/ready")
    Call<Boolean> isPlayerReady(@Path("gameId") String gameId, @Path("playerId") String playerId);
}
