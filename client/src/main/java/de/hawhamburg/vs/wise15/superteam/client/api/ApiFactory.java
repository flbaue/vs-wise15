package de.hawhamburg.vs.wise15.superteam.client.api;

import com.squareup.okhttp.OkHttpClient;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by florian on 12.01.16.
 */
public class ApiFactory {

    private final OkHttpClient httpClient;

    public ApiFactory(OkHttpClient httpClient) {

        this.httpClient = httpClient;
    }

    public GamesAPI getGamesAPI(String baseURI) {
        if (!baseURI.endsWith("/")) {
            baseURI += "/";
        }

        Retrofit GamesServiceRetrofit = new Retrofit.Builder()
                .baseUrl(baseURI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        return GamesServiceRetrofit.create(GamesAPI.class);
    }

    public PlayersAPI getPlayersAPI(String baseURI) {
        if (!baseURI.endsWith("/")) {
            baseURI += "/";
        }

        Retrofit PlayersServiceRetrofit = new Retrofit.Builder()
                .baseUrl(baseURI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        return PlayersServiceRetrofit.create(PlayersAPI.class);
    }

    public BoardsAPI getBoardsAPI(String baseURI) {
        if (!baseURI.endsWith("/")) {
            baseURI += "/";
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        return retrofit.create(BoardsAPI.class);
    }
}
