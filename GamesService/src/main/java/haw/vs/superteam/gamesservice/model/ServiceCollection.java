package haw.vs.superteam.gamesservice.model;

import java.util.List;

/**
 * Created by florian on 16.11.15.
 */
public class ServiceCollection {

    List<String> services;


    public ServiceCollection() {

    }


    public ServiceCollection(List<String> services) {

        this.services = services;
    }


    public List<String> getServices() {

        return services;
    }
}
