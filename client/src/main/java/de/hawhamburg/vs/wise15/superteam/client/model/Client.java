package de.hawhamburg.vs.wise15.superteam.client.model;

/**
 * Created by florian on 09.01.16.
 */
public class Client {
    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
