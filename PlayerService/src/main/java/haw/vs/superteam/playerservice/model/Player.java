package haw.vs.superteam.playerservice.model;

import haw.vs.superteam.playerservice.ClientFacade;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by florian on 13.12.15.
 */
public class Player implements Closeable {
    private final transient ClientFacade clientFacade;
    private int id;
    private transient Socket socket;
    private final transient Client client;

    public Player(ClientFacade clientFacade, Client client) {
        this.clientFacade = clientFacade;
        this.client = client;
    }

    public boolean connectToClient() {

        try {
            socket = new Socket(client.getHost(), client.getPort());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void sendCommand(Command command) throws IOException {
        clientFacade.sendCommand(socket, command);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public void close() throws IOException {
        socket.close();
    }
}
