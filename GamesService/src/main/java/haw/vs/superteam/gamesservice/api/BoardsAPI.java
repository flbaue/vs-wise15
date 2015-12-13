package haw.vs.superteam.gamesservice.api;

import haw.vs.superteam.gamesservice.model.Board;
import haw.vs.superteam.gamesservice.model.Player;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by florian on 07.12.15.
 */
public interface BoardsAPI {

    @PUT("boards/{gameId}")
    Call<Board> createBoard(@Path("gameId") String gameId);

    @PUT("boards/{gameId}/players/{playerId}")
    Call<Void> addPlayer(@Path("gameId") String gameId, @Path("playerId") String playerId, @Body Player player);
}
