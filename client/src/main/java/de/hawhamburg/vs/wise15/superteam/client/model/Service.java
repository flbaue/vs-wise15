package de.hawhamburg.vs.wise15.superteam.client.model;

/**
 * Created by florian on 16.11.15.
 */
public class Service {

    private String name;
    private String description;
    private String service;
    private String uri;


    public Service() {

    }


    public Service(String name, String description, String service, String uri) {

        this.name = name;
        this.description = description;
        this.service = service;
        this.uri = uri;
    }


    public String getName() {

        return name;
    }


    public String getDescription() {

        return description;
    }


    public String getService() {

        return service;
    }


    public String getUri() {

        return uri;
    }
}
