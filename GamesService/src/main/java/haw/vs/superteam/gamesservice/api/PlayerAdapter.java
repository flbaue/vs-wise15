package haw.vs.superteam.gamesservice.api;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import haw.vs.superteam.gamesservice.Utils;
import haw.vs.superteam.gamesservice.model.Event;
import haw.vs.superteam.gamesservice.model.Game;
import haw.vs.superteam.gamesservice.model.Player;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import java.io.IOException;

/**
 * Created by florian on 11.01.16.
 */
public class PlayerAdapter {
    public void turn(Player player) {
        try {
            Unirest.post(player.getUri() + "/turn").asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public void gameStart(Game game) {
        for (Player player : game.getPlayers().getPlayers()) {
            PlayerAPI playerAPI = getPlayerAPI(player);

            Event event = new Event();
            event.setName("GAME_STARTED");
            Event[] events = {event};
            try {
                playerAPI.event(events).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private PlayerAPI getPlayerAPI(Player player) {

        if (player.getAPI() == null) {

            Retrofit playerServiceRetrofit = new Retrofit.Builder()
                    .baseUrl(player.getUri() + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(Utils.getUnsafeOkHttpClient())
                    .build();

            PlayerAPI playerAPI = playerServiceRetrofit.create(PlayerAPI.class);
            player.setAPI(playerAPI);
        }
        return player.getAPI();
    }
}
