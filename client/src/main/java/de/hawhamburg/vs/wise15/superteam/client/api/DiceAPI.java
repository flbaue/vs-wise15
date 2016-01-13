package de.hawhamburg.vs.wise15.superteam.client.api;

import de.hawhamburg.vs.wise15.superteam.client.model.Roll;
import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by florian on 12.01.16.
 */
public interface DiceAPI {

    @GET("dice")
    Call<Roll> rollDice();
}
