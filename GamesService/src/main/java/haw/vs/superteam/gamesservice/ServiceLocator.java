package haw.vs.superteam.gamesservice;

import haw.vs.superteam.gamesservice.api.ServicesAPI;
import haw.vs.superteam.gamesservice.model.Service;
import retrofit.Response;

import java.io.IOException;

/**
 * Created by florian on 13.12.15.
 */
public class ServiceLocator {

    private ServicesAPI servicesAPI;

    public ServiceLocator(ServicesAPI servicesAPI) {

        this.servicesAPI = servicesAPI;
    }

    public Service getBoardsService() throws IOException {
        Response<Service> response = servicesAPI.getService(Constants.BOARDS_SERVICE_NAME).execute();
        return response.body();
    }
}
