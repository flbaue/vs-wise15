package de.hawhamburg.vs.wise15.superteam.client;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import de.hawhamburg.vs.wise15.superteam.client.model.Service;
import de.hawhamburg.vs.wise15.superteam.client.model.ServiceCollection;

import java.io.IOException;

/**
 * Created by florian on 16.11.15.
 */
public class ServiceDirectory {


    private final OkHttpClient httpClient;
    private final Gson gson = new Gson();

    private Service gameService;


    public ServiceDirectory(OkHttpClient httpClient) throws IOException {

        this.httpClient = httpClient;

        String url = Constants.SERVICE_DIRECTORY_URL + "/services/of/type/games";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful()) {

            ServiceCollection serviceCollection = gson.fromJson(response.body().charStream(), ServiceCollection.class);
            gameService = chooseService(serviceCollection);
        } else {
            throw new IOException("Connection to directory service was not successful");
        }
    }


    public String getGameServiceUri() {

        return gameService.getUri();
    }


    private Service chooseService(ServiceCollection serviceCollection) throws IOException {

        for (String service : serviceCollection.getServices()) {
            Request request = new Request.Builder()
                    .url(Constants.SERVICE_DIRECTORY_URL + service)
                    .get()
                    .build();

            try {
                Response response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    return gson.fromJson(response.body().charStream(), Service.class);
                }
            } catch (IOException e) {
                continue;
            }
        }

        throw new IOException("No Service is available");
    }
}
