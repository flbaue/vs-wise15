package haw.vs.superteam.playerservice;

import haw.vs.superteam.playerservice.model.Client;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by florian on 14.12.15.
 */
public class PlayerControllerTest {

    @Test
    public void testConnectClient() {
        ClientFacadeMock clientFacade = new ClientFacadeMock();
        clientFacade.expectedHost = "test";

        PlayerController controller = new PlayerController(clientFacade);
        assertTrue(controller.addPlayer(new Client("test", 123)));
    }
}