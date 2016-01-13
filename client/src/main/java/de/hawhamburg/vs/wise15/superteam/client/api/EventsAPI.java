package de.hawhamburg.vs.wise15.superteam.client.api;

import de.hawhamburg.vs.wise15.superteam.client.model.Subscription;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by florian on 12.01.16.
 */
public interface EventsAPI {

    @POST("events/subscriptions")
    Call<Void> createSubscription(@Body Subscription subscription);
}
