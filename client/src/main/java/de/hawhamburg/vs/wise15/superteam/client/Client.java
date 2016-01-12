package de.hawhamburg.vs.wise15.superteam.client;

import com.squareup.okhttp.OkHttpClient;
import de.hawhamburg.vs.wise15.superteam.client.api.ApiFactory;
import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.api.PlayersAPI;
import de.hawhamburg.vs.wise15.superteam.client.model.Components;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.Player;
import de.hawhamburg.vs.wise15.superteam.client.ui.*;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import javax.swing.*;
import java.awt.*;

/**
 * Created by florian on 16.11.15.
 */
public class Client {

    public final PlayerServiceController playerServiceController;
    public final Components components;
    private final StartForm startForm;
    private final SearchForm searchForm;
    private final CreateForm createForm;
    private final LobbyForm lobbyForm;
    private final GameFormSimple gameForm;
    private final SettingsForm settingsForm;
    private JFrame frame;


    public Client(boolean local, int localServerPort) {

        components = new Components(local, new ApiFactory(Utils.getUnsafeOkHttpClient()));

//        OkHttpClient httpClient = Utils.getUnsafeOkHttpClient();
//        Retrofit serviceRetrofit = new Retrofit.Builder()
//                .baseUrl(Constants.SERVICE_DIRECTORY_URL + "/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient)
//                .build();

        //ComponentsLocator componentsLocator = new ComponentsLocator(serviceRetrofit.create(YellowPagesAPI.class));

        //Service gamesService = componentsLocator.getGamesService(local);
//        Retrofit GamesServiceRetrofit = new Retrofit.Builder()
//                .baseUrl(components.getGame() + "/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient)
//                .build();


        //Service playerService = componentsLocator.getPlayerService(local);
//        Retrofit playerServiceRetrofit = new Retrofit.Builder()
//                .baseUrl(components.getPlayer() + "/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient)
//                .build();

        PlayerServiceFacade playerServiceFacade = new PlayerServiceFacade(components.getPlayersAPI(), this);
        playerServiceController = new PlayerServiceController(components.getPlayer(), playerServiceFacade, localServerPort);
        playerServiceController.startListening();
        playerServiceController.addCommandListener("TURN", (a) -> System.out.println("TURN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));

        startForm = new StartForm(this);
        searchForm = new SearchForm(this, components);
        createForm = new CreateForm(this, components);
        lobbyForm = new LobbyForm(this, components);
        gameForm = new GameFormSimple(this);
        settingsForm = new SettingsForm(this, components);

        System.out.println(components);
    }


    public static void main(String[] args) {

        if (args.length > 0) {
            new Client(Boolean.parseBoolean(args[0]), Integer.parseInt(args[1])).run();
        } else {
            new Client(false, Constants.LOCAL_SERVER_PORT).run();
        }
    }


    private void run() {
        frame = new JFrame("RESTopoly");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.getContentPane().add(new StartForm(this).getPanel());
        frame.setVisible(true);

        //openGameForm(new Game(), new Player()); // test
    }


    public void openSearchForm() {

        searchForm.willAppear();
        changeContentPane(searchForm.getPanel());
    }


    public void openCreateForm() {

        createForm.willAppear();
        changeContentPane(createForm.getPanel());
    }


    public void openStartForm() {

        startForm.willAppear();
        changeContentPane(startForm.getPanel());
    }

    public void openSettingsForm() {

        settingsForm.willAppear();
        changeContentPane(settingsForm.getPanel());
    }


    public void openLobbyForm(Game game, Player player) {


        lobbyForm.setGame(game);
        lobbyForm.setPlayer(player);
        lobbyForm.willAppear();
        changeContentPane(lobbyForm.getPanel());
    }


    public void openGameForm(Game game, Player player) {

        gameForm.setGame(game);
        gameForm.setPlayer(player);
        changeContentPane(gameForm.getPanel());
    }


    private void changeContentPane(Container container) {

        frame.getContentPane().removeAll();
        frame.getContentPane().add(container);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public void couldNotConnectToPlayerService(String s) {
        JOptionPane.showMessageDialog(frame,
                "Could not connect to the PlayerService.\nSee log for details.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
