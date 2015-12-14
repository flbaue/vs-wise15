package haw.vs.superteam.playerservice;

import com.google.gson.Gson;
import haw.vs.superteam.playerservice.model.Client;
import haw.vs.superteam.playerservice.model.Command;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by florian on 14.12.15.
 */
public class ClientFacade {
    private static Gson gson = new Gson();
    private Socket clientSocket;

    public void sendCommand(Command command) {
        try (OutputStreamWriter out =
                     new OutputStreamWriter(
                             new BufferedOutputStream(clientSocket.getOutputStream()))) {

            out.write(gson.toJson(command));
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
