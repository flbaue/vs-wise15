package haw.vs.superteam.playerservice;

import com.google.gson.Gson;
import haw.vs.superteam.playerservice.model.Client;
import haw.vs.superteam.playerservice.model.Command;
import haw.vs.superteam.playerservice.model.Event;
import haw.vs.superteam.playerservice.model.Player;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by florian on 13.12.15.
 */
public class PlayerController {
    private static Gson gson = new Gson();
    private static AtomicInteger playerCounter = new AtomicInteger(0);
    private final ClientFacade clientFacade;
    private Set<Player> players = new HashSet<>();

    public PlayerController(ClientFacade clientFacade) {
        this.clientFacade = clientFacade;
    }

    public Player getPlayerDetails(int playerId) {
        return getPlayerById(playerId);
    }

    public void playerTurn(int playerId) {
        Player player = getPlayerById(playerId);
        if (player != null) {
            try {
                player.sendCommand(new Command("TURN", null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void playerEvent(int playerId, Event[] events) {
        Player player = getPlayerById(playerId);
        if (player != null) {
            try {
                player.sendCommand(new Command("EVENTS", gson.toJson(events)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int addPlayer(Client client) {
        Player player = new Player(clientFacade, client);
        if (player.connectToClient()) {
            int id = playerCounter.getAndIncrement();
            player.setId(id);
            players.add(player);
            return id;
        } else {
            return -1;
        }
    }


    public void deletePlayer(int playerId) {
        Player player = getPlayerById(playerId);
        if (player != null) {
            try {
                player.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            players.remove(player);
        }
    }

    private Player getPlayerById(int playerId) {
        return players.stream().filter(p -> p.getId() == playerId).findAny().orElse(null);
    }
}
