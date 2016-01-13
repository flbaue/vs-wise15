package de.hawhamburg.vs.wise15.superteam.client.model;

import de.hawhamburg.vs.wise15.superteam.client.Utils;
import de.hawhamburg.vs.wise15.superteam.client.api.ApiFactory;
import de.hawhamburg.vs.wise15.superteam.client.api.GamesAPI;
import de.hawhamburg.vs.wise15.superteam.client.api.PlayersAPI;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by florian on 16.11.15.
 */
public class Components {
    private String game;
    private transient GamesAPI gamesAPI;
    private String dice;
    private String board;
    private String bank;
    private String broker;
    private String decks;
    private String events;
    private transient String player;
    private transient PlayersAPI playersAPI;
    private transient ApiFactory apiFactory;

    public Components() {
        apiFactory = new ApiFactory(Utils.getUnsafeOkHttpClient());
    }


    public Components(String game, String dice, String board, String bank, String broker, String decks, String events, String player) {
        apiFactory = new ApiFactory(Utils.getUnsafeOkHttpClient());
        this.game = game;
        this.dice = dice;
        this.board = board;
        this.bank = bank;
        this.broker = broker;
        this.decks = decks;
        this.events = events;
        this.player = player;
    }

    public Components(boolean local, ApiFactory apiFactory) {
        this.apiFactory = apiFactory;
        if (local) {
            setGame("http://192.168.99.100:4502");
            setPlayer("http://192.168.99.100:4500");
            dice = "http://192.168.99.100:4503";
            board = "http://192.168.99.100:4501";
            bank = "http://192.168.99.100:4504";
            broker = "http://192.168.99.100:4505";
            decks = "";
            events = "http://192.168.99.100:4506";
        } else {
            throw new NotImplementedException();
        }
    }

    public PlayersAPI getPlayersAPI() {
        return playersAPI;
    }

    public GamesAPI getGamesAPI() {
        if (gamesAPI == null) {
            setGame(getGame());
        }
        return gamesAPI;
    }

    public void setApiFactory(ApiFactory apiFactory) {
        this.apiFactory = apiFactory;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
        playersAPI = apiFactory.getPlayersAPI(this.player);
    }

    public String getGame() {

        return game;
    }

    public void setGame(String game) {
        this.game = game;
        gamesAPI = apiFactory.getGamesAPI(this.game);
    }

    public String getDice() {

        return dice;
    }

    public void setDice(String dice) {
        this.dice = dice;
    }

    public String getBoard() {

        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getBank() {

        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBroker() {

        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getDecks() {

        return decks;
    }

    public void setDecks(String decks) {
        this.decks = decks;
    }

    public String getEvents() {

        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public void set(Components components) {
        game = components.getGame();
        dice = components.getDice();
        board = components.getBoard();
        bank = components.getBank();
        broker = components.getBroker();
        decks = components.getDecks();
        events = components.getEvents();
    }

    @Override
    public String toString() {
        return "Components:\ngames_service: " + game;
    }
}
