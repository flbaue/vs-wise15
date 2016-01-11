package haw.vs.superteam.gamesservice.api;

import haw.vs.superteam.gamesservice.Utils;
import haw.vs.superteam.gamesservice.model.Board;
import haw.vs.superteam.gamesservice.model.Game;
import haw.vs.superteam.gamesservice.model.Player;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.IOException;

/**
 * Created by florian on 11.01.16.
 */
public class BoardsAdapter {

    public Board createBoard(Game game) {

        BoardsAPI boardsAPI = getBoardsAPI(game.getComponents().getBoard());
        Response<Board> response = null;
        try {
            response = boardsAPI.createBoard(game.getGameid()).execute();
        } catch (IOException e) {
            e.printStackTrace();

        }
        if (response != null && response.isSuccess()) {
            return response.body();
        } else {
            return null;
        }
    }


    public boolean addPlayer(Game game, Player player) {
        BoardsAPI boardsAPI = getBoardsAPI(game.getComponents().getBoard());
        Response<Void> response = null;
        try {
            response = boardsAPI.addPlayer(game.getGameid(), player.getId(), player).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null && response.isSuccess()) {
            return true;
        } else {
            return false;
        }
    }

    private BoardsAPI getBoardsAPI(String board) {
        Retrofit boardsServiceRetrofit = new Retrofit.Builder()
                .baseUrl(board + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(Utils.getUnsafeOkHttpClient())
                .build();

        return boardsServiceRetrofit.create(BoardsAPI.class);
    }
}
