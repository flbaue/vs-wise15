package de.hawhamburg.vs.wise15.superteam.client.service;

import com.squareup.okhttp.OkHttpClient;
import de.hawhamburg.vs.wise15.superteam.client.api.YellowPagesAPI;

/**
 * Created by florian on 19.11.15.
 */
public class ComponentsLocatorService {

    private ServiceComponents serviceComponents;
    private YellowPagesAPI yellowPagesAPI;
    private OkHttpClient httpClient;

    public ComponentsLocatorService holder(ServiceComponents serviceComponents) {
        this.serviceComponents = serviceComponents;
        return this;
    }

    public ComponentsLocatorService api(YellowPagesAPI yellowPagesAPI) {
        this.yellowPagesAPI = yellowPagesAPI;
        return this;
    }

    public ComponentsLocatorService httpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public void start() {
        new ComponentFetcher(ComponentType.games, yellowPagesAPI, httpClient,
                (s, t) -> {
                    serviceComponents.setService(s, t);
                }).execute();
    }
}
