package haw.vs.superteam.playerservice;

import haw.vs.superteam.playerservice.model.Client;
import haw.vs.superteam.playerservice.model.Event;
import haw.vs.superteam.playerservice.model.Player;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by florian on 13.12.15.
 */
public class PlayerController {
    private Socket clientSocket;
    private Player playerDetails;

    public PlayerController() {

    }

    public Player getPlayerDetails() {
        return playerDetails;
    }

    public void playerTurn() {

    }

    public void playerEvent(Event[] events) {

    }

    public boolean connectClient(Client client) {
        try {
            clientSocket = new Socket(client.getHost(), client.getPort());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
