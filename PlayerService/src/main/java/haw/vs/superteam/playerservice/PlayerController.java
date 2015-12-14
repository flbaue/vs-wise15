package haw.vs.superteam.playerservice;

import com.google.gson.Gson;
import haw.vs.superteam.playerservice.model.Client;
import haw.vs.superteam.playerservice.model.Command;
import haw.vs.superteam.playerservice.model.Event;
import haw.vs.superteam.playerservice.model.Player;

/**
 * Created by florian on 13.12.15.
 */
public class PlayerController {
    private static Gson gson = new Gson();
    private final ClientFacade clientFacade;

    public PlayerController(ClientFacade clientFacade) {


        this.clientFacade = clientFacade;
    }

    public Player getPlayerDetails() {
        return
    }

    public void playerTurn() {
        clientFacade.sendCommand(new Command("TURN", ""));
    }

    public void playerEvent(Event[] events) {
        clientFacade.sendCommand(new Command("EVENTS", gson.toJson(events)));
    }

    public boolean connectClient(Client client) {
        return clientFacade.connectClient(client);
    }


}
