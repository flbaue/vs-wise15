package haw.vs.superteam.gamesservice.api;

import haw.vs.superteam.gamesservice.model.Event;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by florian on 11.01.16.
 */
public interface PlayerAPI {

    @POST("turn")
    Call<Void> turn();

    @POST("event")
    Call<Void> event(@Body Event[] events);
}
