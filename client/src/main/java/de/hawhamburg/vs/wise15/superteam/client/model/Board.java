package de.hawhamburg.vs.wise15.superteam.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by florian on 13.01.16.
 */
public class Board {
    public List<Field> fields = new ArrayList<>();
    public Map<String, Integer> positions = new HashMap<>();
    public List<Game> games = new ArrayList<>();
    public List<Player> players = new ArrayList<>();
}
