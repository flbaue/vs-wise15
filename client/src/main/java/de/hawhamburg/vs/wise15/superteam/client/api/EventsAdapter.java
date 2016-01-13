package de.hawhamburg.vs.wise15.superteam.client.api;

import de.hawhamburg.vs.wise15.superteam.client.Utils;
import de.hawhamburg.vs.wise15.superteam.client.model.Subscription;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.IOException;

/**
 * Created by florian on 12.01.16.
 */
public class EventsAdapter {

    public void createSubscription(Subscription subscription, String eventsURI) throws IOException {
        EventsAPI eventsAPI = getEventsAPI(eventsURI);
        Response<Void> response = eventsAPI.createSubscription(subscription).execute();
    }

    private EventsAPI getEventsAPI(String uri) {
        if (!uri.endsWith("/")) {
            uri += "/";
        }

        Retrofit eventsServiceRetrofit = new Retrofit.Builder()
                .baseUrl(uri)
                .addConverterFactory(GsonConverterFactory.create())
                .client(Utils.getUnsafeOkHttpClient())
                .build();

        return eventsServiceRetrofit.create(EventsAPI.class);
    }
}
