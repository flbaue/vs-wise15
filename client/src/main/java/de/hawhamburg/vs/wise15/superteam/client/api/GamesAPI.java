package de.hawhamburg.vs.wise15.superteam.client.api;

import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.GameCollection;
import de.hawhamburg.vs.wise15.superteam.client.model.ServiceCollection;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by florian on 16.11.15.
 */
public interface GamesAPI {

    @POST("games")
    Call<Game> createGame();

    @GET("games")
    Call<GameCollection> all();

    @GET("games/{gameid}")
    Call<Game> byId(@Path("gameid") String gameId);
}
