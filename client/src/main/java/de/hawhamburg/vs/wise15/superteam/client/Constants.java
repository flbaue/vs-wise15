package de.hawhamburg.vs.wise15.superteam.client;

import com.squareup.okhttp.MediaType;

/**
 * Created by florian on 16.11.15.
 */
public class Constants {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final String SERVICE_DIRECTORY_URL = "https://vs-docker.informatik.haw-hamburg.de/ports/8053";
    //public static final String SERVICE_DIRECTORY_URL = "http://127.0.0.1:4567";

    public static final String GAMES_SERVICE_NAME = "SuperTeamGamesService";
    public static final String PLAYER_SERVICE_NAME = "SuperTeamPlayersService";

    public static final int LOCAL_SERVER_PORT = 11111;
}
