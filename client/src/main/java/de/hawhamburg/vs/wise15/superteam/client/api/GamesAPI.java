package de.hawhamburg.vs.wise15.superteam.client.api;

import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.GameCollection;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import de.hawhamburg.vs.wise15.superteam.client.model.PlayerCollection;
import retrofit.Call;
import retrofit.http.*;

/**
 * Created by florian on 16.11.15.
 */
public interface GamesAPI {

    /**
     * Gets all {@link Game} objects.
     *
     * @return {@link GameCollection}
     */
    @GET("games")
    Call<GameCollection> all();


    /**
     * Creates a {@link Game}.
     *
     * @param game {@link Game}
     * @return {@link Game}
     */
    @POST("games")
    Call<Game> createGame(@Body Game game);


    /**
     * Gets the {@link Game}.
     *
     * @param gameId of the game.
     * @return {@link Game}
     */
    @GET("games/{gameId}")
    Call<Game> byId(@Path("gameId") String gameId);


    /**
     * Gets all {@link Player} that take part in the {@link Game}.
     *
     * @param gameId of the game.
     * @return {@link PlayerCollection}
     */
    @GET("games/{gameId}/players")
    Call<PlayerCollection> allPlayers(@Path("gameId") String gameId);


    /**
     * Gets the {@link Player} from the {@link Game}.
     *
     * @param gameId of the game.
     * @param playerId of the player.
     * @return {@link Player}
     */
    @GET("games/{gameId}/players/{playerId}")
    Call<Player> getPlayer(@Path("gameId") String gameId, @Path("playerId") String playerId);


    /**
     * Joins the {@link Player} to the {@link Game}.
     *
     * @param gameId of the game.
     * @param playerId of the player.
     * @param playerName of the player.
     * @param playerURI of the player.
     * @return Void
     */
    @PUT("games/{gameId}/players/{playerId}")
    Call<Void> joinPlayer(@Path("gameId") String gameId, @Path("playerId") String playerId,
                          @Query("name") String playerName, @Query("uri") String playerURI);

    /**
     * Removes the {@link Player} from the {@link Game}.
     *
     * @param gameId of the game.
     * @param playerId of the player.
     * @return Void
     */
    @DELETE("games/{gameId}/players/{playerId}")
    Call<Void> deletePlayer(@Path("gameId") String gameId, @Path("playerId") String playerId);


    /**
     * Sets a {@link Player} ready for the {@link Game}.
     *
     * @param gameId of the game.
     * @param playerId of the player.
     * @return Void
     */
    @PUT("games/{gameId}/players/{playerId}/ready")
    Call<Void> setPlayerReady(@Path("gameId") String gameId, @Path("playerId") String playerId);


    /**
     * Gets the ready status of a {@link Player} for a {@link Game}.
     *
     * @param gameId of the game.
     * @param playerId of the player.
     * @return boolean
     */
    @GET("games/{gameId}/players/{playerId}/ready")
    Call<Boolean> isPlayerReady(@Path("gameId") String gameId, @Path("playerId") String playerId);


    /**
     * Gets the {@link Player} that is active and has to take action.
     *
     * @param gameId of the game.
     * @return {@link Player}
     */
    @GET("/games/{gameId}/players/current")
    Call<Player> currentPlayer(@Path("gameId") String gameId);


    /**
     * Gets the {@link Player} that is holding the turn mutex.
     *
     * @param gameId of the game.
     * @return {@link Player}
     */
    @GET("games/{gameid}/players/turn")
    Call<Player> getTurnPlayer(@Path("gameId") String gameId);


    /**
     * Sets the turn mutex for the {@link Player}.
     *
     * @param gameId of the game.
     * @param playerId of the player.
     * @param player {@link Player}
     * @return Void
     */
    @PUT("games/{gameid}/players/turn")
    Call<Void> setTurnPlayer(@Path("gameId") String gameId, @Query("player") String playerId, @Body Player player);


    /**
     * Deletes the turn mutex for the {@link Player}.
     *
     * @param gameId of the game.
     * @return Void
     */
    @DELETE("games/{gameid}/players/turn")
    Call<Void> removeTurnPlayer(@Path("gameId") String gameId);
}
