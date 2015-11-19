package de.hawhamburg.vs.wise15.superteam.client.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import de.hawhamburg.vs.wise15.superteam.client.api.YellowPagesAPI;
import de.hawhamburg.vs.wise15.superteam.client.model.Service;
import de.hawhamburg.vs.wise15.superteam.client.model.ServiceCollection;
import retrofit.Response;
import retrofit.Retrofit;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by florian on 19.11.15.
 */
class ComponentFetcher extends SwingWorker<Void, Void> {

    private final ComponentType type;
    private final OkHttpClient httpClient;
    private final BiConsumer<Service, ComponentType> callback;
    private final YellowPagesAPI yellowPagesAPI;
    private Service service;

    public ComponentFetcher(ComponentType type,
                            YellowPagesAPI yellowPagesAPI,
                            OkHttpClient httpClient,
                            BiConsumer<Service, ComponentType> callback) {
        super();

        this.type = type;
        this.httpClient = httpClient;
        this.callback = callback;
        this.yellowPagesAPI = yellowPagesAPI;
    }

    @Override
    protected Void doInBackground() throws Exception {

        Response<ServiceCollection> response = yellowPagesAPI.servicesOfType(type.name()).execute();
        service = chooseService(response);
        return null;
    }

    private Service chooseService(Response<ServiceCollection> response) {
        if (!response.isSuccess()) {
            return null;
        }

        ServiceCollection services = response.body();
        List<String> serviceIds = services.getServiceIds();

        for (String id : serviceIds) {
            try {
                Response<Service> serviceResponse = yellowPagesAPI.byId(id).execute();
                if (!serviceResponse.isSuccess()) {
                    continue;
                }
                Service service = serviceResponse.body();

                Request request = new Request.Builder().get().url(service.getUri()).build();
                if (httpClient.newCall(request).execute().isSuccessful()) {
                    return service;
                }
            } catch (IOException e) {
                //continue;
            }
        }

        return null;
    }

    @Override
    protected void done() {
        callback.accept(service, type);
    }
}
