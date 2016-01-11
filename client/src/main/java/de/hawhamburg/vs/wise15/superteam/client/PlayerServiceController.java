package de.hawhamburg.vs.wise15.superteam.client;

import de.hawhamburg.vs.wise15.superteam.client.callback.CallbackA;
import de.hawhamburg.vs.wise15.superteam.client.worker.CommandListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by florian on 09.01.16.
 */
public class PlayerServiceController {

    private final String playerService;
    private final int playerId;
    private Map<String, List<CallbackA<String>>> listenerMap = new HashMap<>();
    private PlayerServiceFacade facade;
    private Thread listenerThread;

    public PlayerServiceController(String playerService, PlayerServiceFacade playerServiceFacade, int localServerPort) {
        this.playerService = playerService;
        this.facade = playerServiceFacade;

        playerId = playerServiceFacade.connectWithPlayerService(localServerPort);
    }

    public void addCommandListener(String command, CallbackA<String> listener) {
        if (listenerMap.containsKey(command)) {
            listenerMap.get(command).add(listener);
        } else {
            List<CallbackA<String>> list = new LinkedList<>();
            list.add(listener);
            listenerMap.put(command, list);
        }
    }

    public void startListening() {
        listenerThread = new Thread(new CommandListener(facade, listenerMap));
        listenerThread.start();
    }

    public String getUri() {
        String uri = playerService + "/player/" + playerId;
        System.out.println("Player URI: " + uri);
        return uri;
    }
}
