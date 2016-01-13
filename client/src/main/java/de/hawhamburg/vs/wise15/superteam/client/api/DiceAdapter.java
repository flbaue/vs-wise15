package de.hawhamburg.vs.wise15.superteam.client.api;

import de.hawhamburg.vs.wise15.superteam.client.Utils;
import de.hawhamburg.vs.wise15.superteam.client.model.Roll;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.IOException;

/**
 * Created by florian on 12.01.16.
 */
public class DiceAdapter {

    public Roll rollDice(String uri) throws IOException {
        DiceAPI diceAPI = getDiceAPI(uri);
        Response<Roll> response = diceAPI.rollDice().execute();
        if (response.isSuccess()) {
            return response.body();
        } else {
            return null;
        }
    }

    private DiceAPI getDiceAPI(String uri) {
        if (!uri.endsWith("/")) {
            uri += "/";
        }

        Retrofit diceServiceRetrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .addConverterFactory(GsonConverterFactory.create())
                .client(Utils.getUnsafeOkHttpClient())
                .build();

        return diceServiceRetrofit.create(DiceAPI.class);
    }
}
