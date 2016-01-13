package haw.vs.superteam.gamesservice.api;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import haw.vs.superteam.gamesservice.Utils;
import haw.vs.superteam.gamesservice.model.Event;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.IOException;


/**
 * Created by florian on 12.01.16.
 */
public class EventsAdapter {

    private Gson gson = new Gson();

    public void sendEvent(String gameId, Event event, String eventServiceURI) throws IOException {
        EventsAPI eventsAPI = getEventsAPI(eventServiceURI);
        Response<Void> response = eventsAPI.createEvent(gameId, event).execute();
        System.out.println(response.message());

//        try {
//            Unirest.post(eventServiceURI + "/events").body(gson.toJson(event)).asString();
//        } catch (UnirestException e) {
//            e.printStackTrace();
//        }

    }

    private EventsAPI getEventsAPI(String eventServiceURI) {
        if (!eventServiceURI.endsWith("/")) {
            eventServiceURI += "/";
        }

        Retrofit eventsServiceRetrofit = new Retrofit.Builder()
                .baseUrl(eventServiceURI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(Utils.getUnsafeOkHttpClient())
                .build();

        return eventsServiceRetrofit.create(EventsAPI.class);
    }


}
