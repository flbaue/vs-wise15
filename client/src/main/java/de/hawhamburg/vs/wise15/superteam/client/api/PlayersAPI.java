package de.hawhamburg.vs.wise15.superteam.client.api;

import de.hawhamburg.vs.wise15.superteam.client.model.Client;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by florian on 16.11.15.
 */
public interface PlayersAPI {

    // TODO rest api?

    @POST("player")
    Call<Integer> connectPlayer(@Body Client client);

}
