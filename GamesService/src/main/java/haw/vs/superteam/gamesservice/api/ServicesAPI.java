package haw.vs.superteam.gamesservice.api;

import haw.vs.superteam.gamesservice.model.Service;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by florian on 13.12.15.
 */
public interface ServicesAPI {

    @GET("services/of/name/{serviceName}")
    Call<Service> getService(@Path("serviceName") String serviceName);
}
