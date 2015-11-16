package de.hawhamburg.vs.wise15.superteam.client.api;

import de.hawhamburg.vs.wise15.superteam.client.model.Service;
import de.hawhamburg.vs.wise15.superteam.client.model.ServiceCollection;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by florian on 16.11.15.
 */
public interface YellowPagesAPI {

    @GET("services/of/type/{type}")
    Call<ServiceCollection> servicesOfType(@Path("type") String type);

    @GET("services/{serviceId}")
    Call<Service> byId(@Path("serviceId") String serviceId);
}
