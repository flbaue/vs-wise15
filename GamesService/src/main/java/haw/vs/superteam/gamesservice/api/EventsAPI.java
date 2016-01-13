package haw.vs.superteam.gamesservice.api;

import haw.vs.superteam.gamesservice.model.Event;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by florian on 12.01.16.
 */
public interface EventsAPI {

    @POST("events")
    Call<Void> createEvent(@Query("gameId") String gameId, @Body Event event);
}
