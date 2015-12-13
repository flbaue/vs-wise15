package haw.vs.superteam.gamesservice;

import haw.vs.superteam.gamesservice.api.ServicesAPI;
import haw.vs.superteam.gamesservice.model.Service;
import haw.vs.superteam.gamesservice.model.ServiceCollection;
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
        Response<ServiceCollection> serviceCollectionResponse = servicesAPI.getServiceCollection(Constants.BOARDS_SERVICE_NAME).execute();
        ServiceCollection serviceCollection = serviceCollectionResponse.body();
        String uri = serviceCollection.getServices().get(serviceCollection.getServices().size() - 1);

        int i = uri.lastIndexOf("/");
        String id = uri.substring(i);

        Response<Service> serviceResponse = servicesAPI.getServiceById(id).execute();
        return serviceResponse.body();
    }
}
