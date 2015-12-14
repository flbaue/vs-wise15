package haw.vs.superteam.playerservice;

import haw.vs.superteam.playerservice.model.Client;
import haw.vs.superteam.playerservice.model.Command;

import static org.junit.Assert.assertEquals;

/**
 * Created by florian on 14.12.15.
 */
public class ClientFacadeMock extends ClientFacade {

    public String expectedCommand;
    public String expectedHost;

    public void sendCommand(Command command) {
        assertEquals(command.getCommand(), expectedCommand);
    }

    public boolean connectClient(Client client) {
        assertEquals(client.getHost(), expectedHost);
        return true;
    }
}
