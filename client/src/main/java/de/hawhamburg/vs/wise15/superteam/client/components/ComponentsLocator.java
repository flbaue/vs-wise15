package de.hawhamburg.vs.wise15.superteam.client.components;

import com.squareup.okhttp.OkHttpClient;
import de.hawhamburg.vs.wise15.superteam.client.api.YellowPagesAPI;

/**
 * Created by florian on 19.11.15.
 */
public class ComponentsLocator {

    private ComponentsHolder componentsHolder;
    private YellowPagesAPI yellowPagesAPI;
    private OkHttpClient httpClient;

    public ComponentsLocator holder(ComponentsHolder componentsHolder) {
        this.componentsHolder = componentsHolder;
        return this;
    }

    public ComponentsLocator api(YellowPagesAPI yellowPagesAPI) {
        this.yellowPagesAPI = yellowPagesAPI;
        return this;
    }

    public ComponentsLocator httpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public void start() {
        new ComponentFetcher(ComponentType.games, yellowPagesAPI, httpClient,
                (s, t) -> {
                    componentsHolder.setService(s, t);
                }).execute();
    }
}
