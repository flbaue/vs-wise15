package de.hawhamburg.vs.wise15.superteam.client.worker;

import de.hawhamburg.vs.wise15.superteam.client.PlayerServiceFacade;
import de.hawhamburg.vs.wise15.superteam.client.callback.CallbackA;
import de.hawhamburg.vs.wise15.superteam.client.model.Command;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by florian on 09.01.16.
 */
public class CommandListener implements Runnable {


    private final PlayerServiceFacade facade;
    private final Map<String, List<CallbackA<String>>> listenerMap;

    public CommandListener(PlayerServiceFacade facade, Map<String, List<CallbackA<String>>> ListenerMap) {

        this.facade = facade;
        listenerMap = ListenerMap;
    }

    @Override
    public void run() {
        while (facade.isConnected()) {
            Command command = null;
            try {
                command = facade.getCommand();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (listenerMap.containsKey(command.getCommand())) {
                for (CallbackA<String> callback : listenerMap.get(command.getCommand())) {
                    final Command finalCommand = command;
                    SwingUtilities.invokeLater(() -> callback.callback(finalCommand.getContent()));
                }
            }
        }
    }
}
