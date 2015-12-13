package haw.vs.superteam.playerservice;

import haw.vs.superteam.playerservice.model.Event;
import haw.vs.superteam.playerservice.model.Player;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by florian on 13.12.15.
 */
public class PlayerController {
    private Player playerDetails;
    private final Socket clientSocket;

    public PlayerController() {
//        ServerSocket serverSocket = new ServerSocket(44444);
//        clientSocket = serverSocket.accept();
    }

    public Player getPlayerDetails() {
        return playerDetails;
    }

    public void playerTurn() {

    }

    public void playerEvent(Event[] events) {

    }

}
