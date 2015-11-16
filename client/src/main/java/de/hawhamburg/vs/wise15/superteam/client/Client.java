package de.hawhamburg.vs.wise15.superteam.client;

import com.squareup.okhttp.OkHttpClient;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.ui.*;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import javax.swing.*;
import java.awt.*;

/**
 * Created by florian on 16.11.15.
 */
public class Client {

    private final OkHttpClient httpClient = Utils.getUnsafeOkHttpClient();
    private final Retrofit retrofit;
    private final StartForm startForm;
    private final SearchForm searchForm;
    private final CreateForm createForm;
    private final LobbyForm lobbyForm;
    private final GameForm gameForm;
    private JFrame frame;


    public Client() {

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVICE_DIRECTORY_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(this.httpClient)
                .build();

        startForm = new StartForm(this);
        searchForm = new SearchForm(this, retrofit);
        createForm = new CreateForm(this, retrofit);
        lobbyForm = new LobbyForm(this);
        gameForm = new GameForm();
    }


    public static void main(String[] args) {

        new Client().run();
    }


    private void run() {

        frame = new JFrame("RESTopoly");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.getContentPane().add(new StartForm(this).getPanel());
        frame.setVisible(true);
    }


    public void openSearchForm() {

        searchForm.refresh();
        changeContentPane(searchForm.getPanel());
    }


    public void openCreateForm() {

        changeContentPane(createForm.getPanel());
    }


    public void openStartForm() {

        changeContentPane(startForm.getPanel());
    }


    public void openLobbyForm(Game game) {

        lobbyForm.setGame(game);
        changeContentPane(lobbyForm.getPanel());
    }


    private void changeContentPane(Container container) {

        frame.getContentPane().removeAll();
        frame.getContentPane().add(container);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
}
