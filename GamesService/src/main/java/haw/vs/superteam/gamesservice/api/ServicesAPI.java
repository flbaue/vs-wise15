package haw.vs.superteam.gamesservice.api;

import haw.vs.superteam.gamesservice.model.Service;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by florian on 13.12.15.
 */
public interface ServicesAPI {

    @GET("services/of/name/{serviceName}")
    Call<Service> getService(@Path("serviceName") String serviceName);

    @POST("services")
    Call<Void> registerService(@Body Service service);
}
