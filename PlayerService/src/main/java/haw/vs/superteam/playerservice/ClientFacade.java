package haw.vs.superteam.playerservice;

import com.google.gson.Gson;
import haw.vs.superteam.playerservice.model.Command;
import haw.vs.superteam.playerservice.model.Event;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by florian on 14.12.15.
 */
public class ClientFacade {
    private static Gson gson = new Gson();

    public void sendCommand(Socket socket, Command command) throws IOException {
        OutputStreamWriter out =
                new OutputStreamWriter(
                        new BufferedOutputStream(socket.getOutputStream()));

        out.write(gson.toJson(command));
        out.write("\n");
        out.flush();
    }
}
