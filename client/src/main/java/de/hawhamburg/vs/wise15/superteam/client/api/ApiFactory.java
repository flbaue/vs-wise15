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

    public GamesAPI getGamesAPI(String uri) {
        if (!uri.endsWith("/")) {
            uri += "/";
        }

        Retrofit GamesServiceRetrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        return GamesServiceRetrofit.create(GamesAPI.class);
    }

    public PlayersAPI getPlayersAPI(String uri) {
        if (!uri.endsWith("/")) {
            uri += "/";
        }

        Retrofit PlayersServiceRetrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        return PlayersServiceRetrofit.create(PlayersAPI.class);
    }
}
