package haw.vs.superteam.playerservice.model;

import java.net.InetAddress;

/**
 * Created by florian on 14.12.15.
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
