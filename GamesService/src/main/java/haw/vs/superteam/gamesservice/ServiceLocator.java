package haw.vs.superteam.gamesservice;

import com.google.gson.Gson;
import haw.vs.superteam.gamesservice.api.ServicesAPI;
import haw.vs.superteam.gamesservice.model.Service;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.IOException;

/**
 * Created by florian on 13.12.15.
 */
public class ServiceLocator {

    private ServicesAPI servicesAPI;

    public ServiceLocator() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVICE_DIRECTORY_URL + "/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(Utils.getUnsafeOkHttpClient())
                .build();

        servicesAPI = retrofit.create(ServicesAPI.class);
    }

    public Service getBoardsService() throws IOException {
        Response<Service> response = servicesAPI.getService(Constants.BOARDS_SERVICE_NAME).execute();
        return response.body();
    }
}
