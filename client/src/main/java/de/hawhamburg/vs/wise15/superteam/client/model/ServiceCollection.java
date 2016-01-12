package de.hawhamburg.vs.wise15.superteam.client.model;

import java.util.ArrayList;
import java.util.Collections;
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

    public List<String> getServiceIds() {
        if (services == null || services.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<>(services.size());

        services.forEach(s -> {
            String tmp = s;
            if (tmp.endsWith("/")) {
                tmp = tmp.substring(0, tmp.length() - 2);
            }
            int index = s.lastIndexOf('/') + 1;
            tmp = tmp.substring(index);
            list.add(tmp);
        });

        return list;
    }
}
