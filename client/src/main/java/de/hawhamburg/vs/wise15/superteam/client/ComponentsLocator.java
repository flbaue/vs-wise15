package de.hawhamburg.vs.wise15.superteam.client;

import de.hawhamburg.vs.wise15.superteam.client.api.YellowPagesAPI;
import de.hawhamburg.vs.wise15.superteam.client.model.Service;
import de.hawhamburg.vs.wise15.superteam.client.model.ServiceCollection;
import retrofit.Response;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by florian on 14.12.15.
 */
public class ComponentsLocator {

    private final Logger logger = Utils.getLogger(ComponentsLocator.class.getName());
    private final YellowPagesAPI yellowPagesAPI;

    public ComponentsLocator(YellowPagesAPI yellowPagesAPI) {
        this.yellowPagesAPI = yellowPagesAPI;
    }

    public Service getGamesService(boolean local) {
        if (local) {
            return new Service("GamesService", "games service local", "games", "http://192.168.99.100:4502");
        } else {
            try {
                Response<ServiceCollection> collectionResponse = yellowPagesAPI.servicesOfName(Constants.GAMES_SERVICE_NAME).execute();
                List<String> serviceIds = collectionResponse.body().getServiceIds();
                String id = serviceIds.get(serviceIds.size() - 1);
                Response<Service> serviceResponse = yellowPagesAPI.byId(id).execute();
                return serviceResponse.body();
            } catch (Exception e) {
                logger.severe(() -> "Cannot locate GamesService " + e.toString());
                return null;
            }
        }
    }

    public Service getPlayerService(boolean local) {
        if (local) {
            return new Service(Constants.PLAYER_SERVICE_NAME, "local", "player", "http://192.168.99.100:4500");
        } else {
            try {
                Response<ServiceCollection> collectionResponse = yellowPagesAPI.servicesOfName(
                        Constants.PLAYER_SERVICE_NAME).execute();

                List<String> serviceIds = collectionResponse.body().getServiceIds();
                String id = serviceIds.get(serviceIds.size() - 1);
                Response<Service> serviceResponse = yellowPagesAPI.byId(id).execute();
                return serviceResponse.body();
            } catch (Exception e) {
                logger.severe(() -> "Cannot locate PlayerServiceFacade " + e.toString());
                return null;
            }
        }
    }
}
