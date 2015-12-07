package haw.vs.superteam.gamesservice.api;

import haw.vs.superteam.gamesservice.model.Board;
import retrofit.Call;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by florian on 07.12.15.
 */
public interface BoardsAPI {

    @PUT("boards/{gameId}")
    Call<Board> createBoard(@Path("gameId") String gameId);
}
