package de.hawhamburg.vs.wise15.superteam.client;

import com.google.gson.Gson;
import de.hawhamburg.vs.wise15.superteam.client.api.PlayersAPI;
import de.hawhamburg.vs.wise15.superteam.client.model.Client;
import de.hawhamburg.vs.wise15.superteam.client.model.Command;
import retrofit.Response;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by florian on 09.01.16.
 */
public class PlayerServiceFacade {

    public final PlayersAPI api;
    private final de.hawhamburg.vs.wise15.superteam.client.Client client;
    private Socket socket;
    private ServerSocket serverSocket;
    private Gson gson = new Gson();
    private BufferedReader reader;

    public PlayerServiceFacade(PlayersAPI playersAPI, de.hawhamburg.vs.wise15.superteam.client.Client client) {
        this.api = playersAPI;
        this.client = client;
    }

    public int connectWithPlayerService(int localServerPort) {

        Thread thread = new Thread(() -> {
            if (this.serverSocket != null) {
                try {
                    this.serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.serverSocket = null;
            }

            try {
                serverSocket = new ServerSocket(localServerPort);
                this.socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> client.couldNotConnectToPlayerService("Socket Exception"));
            }
        });
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            String localIpAddress = InetAddress.getLocalHost().getHostAddress();
            Client client = new Client(localIpAddress, localServerPort);
            Response<Integer> response = api.connectPlayer(client).execute();
            if (!response.isSuccess()) {
                this.client.couldNotConnectToPlayerService("Service cannot reach client");
            }
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
            client.couldNotConnectToPlayerService("Client cannot reach service");
        }
        return -1;
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public Command getCommand() throws IOException {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        String line = reader.readLine();
        return gson.fromJson(line, Command.class);
    }
}
