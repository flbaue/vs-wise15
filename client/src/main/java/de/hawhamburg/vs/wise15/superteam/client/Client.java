package de.hawhamburg.vs.wise15.superteam.client;

import com.squareup.okhttp.OkHttpClient;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.ui.CreateForm;
import de.hawhamburg.vs.wise15.superteam.client.ui.LobbyForm;
import de.hawhamburg.vs.wise15.superteam.client.ui.SearchForm;
import de.hawhamburg.vs.wise15.superteam.client.ui.StartForm;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import javax.swing.*;

/**
 * Created by florian on 16.11.15.
 */
public class Client {

    private final OkHttpClient httpClient = Utils.getUnsafeOkHttpClient();
    private final Retrofit retrofit;
    private JFrame frame;


    public Client() {

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVICE_DIRECTORY_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(this.httpClient)
                .build();
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

        frame.getContentPane().removeAll();
        frame.getContentPane().add(new SearchForm(this, retrofit).getPanel());
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }


    public void openCreateForm() {

        frame.getContentPane().removeAll();
        frame.getContentPane().add(new CreateForm(this, retrofit).getPanel());
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }


    public void openStartForm() {

        frame.getContentPane().removeAll();
        frame.getContentPane().add(new StartForm(this).getPanel());
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }


    public void openLobbyForm(Game game) {

        frame.getContentPane().removeAll();
        frame.getContentPane().add(new LobbyForm(this, game).getPanel());
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
}
