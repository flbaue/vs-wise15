package haw.vs.superteam.gamesservice.api;

import haw.vs.superteam.gamesservice.model.Service;
import haw.vs.superteam.gamesservice.model.ServiceCollection;
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
    Call<ServiceCollection> getServiceCollection(@Path("serviceName") String serviceName);

    @GET("services/{id}")
    Call<Service> getServiceById(@Path("id") String id);

    @POST("services")
    Call<Void> registerService(@Body Service service);
}
