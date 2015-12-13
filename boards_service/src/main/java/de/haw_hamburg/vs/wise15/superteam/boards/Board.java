package de.haw_hamburg.vs.wise15.superteam.boards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
	
	private ArrayList<Field> fields = new ArrayList<Field>();
	private Map<String, Integer> positions = new HashMap<String, Integer>();
	private ArrayList<Game> games = new ArrayList<Game>();
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public Board() {
	}
	
	public void initializeBoard() {
		ArrayList<String> fieldnames = setFieldnames();
		for(int i = 0; i < fieldnames.size(); i++) {
			Field field = new Field();
			Place place = new Place(fieldnames.get(i));
			field.setPlace(place);
			fields.add(field);
		}
	}
	
	private ArrayList<String> setFieldnames() {
		ArrayList<String> fieldnames = new ArrayList<String>();
		fieldnames.add("LOS");
		fieldnames.add("Badstrasse");
		fieldnames.add("Gemeinschaftsfeld");
		fieldnames.add("Turmstrasse");
		fieldnames.add("Einkommensteuer");
		fieldnames.add("Sued-Bahnhof");
		fieldnames.add("Chausseestrasse");
		fieldnames.add("Ereignis-Feld");
		fieldnames.add("Elisenstrasse");
		fieldnames.add("Poststrasse");
		fieldnames.add("Gefaegnis");
		fieldnames.add("Seestrasse");
		fieldnames.add("Elektrizitaetswerk");
		fieldnames.add("Hafenstrasse");
		fieldnames.add("Neue Strasse");
		fieldnames.add("West-Bahnhof");
		fieldnames.add("Muenchnerstrasse");
		fieldnames.add("Gemeinschaftsfeld");
		fieldnames.add("Wienerstrasse");
		fieldnames.add("Berlinerstrasse");
		fieldnames.add("Frei parken");
		fieldnames.add("Theaterstrasse");
		fieldnames.add("Ereignis-Feld");
		fieldnames.add("Museumstrasse");
		fieldnames.add("Opernplatz");
		fieldnames.add("Nord-Bahnhof");
		fieldnames.add("Lessingstrasse");
		fieldnames.add("Schillerstrasse");
		fieldnames.add("Wasserwerk");
		fieldnames.add("Goethestrasse");
		fieldnames.add("Gehen sie in das Gefaegnis");
		fieldnames.add("Rathausplatz");
		fieldnames.add("Hauptstrasse");
		fieldnames.add("Gemeinschaftsfeld");
		fieldnames.add("Bahnhofstrasse");
		fieldnames.add("Hauptbahnhof");
		fieldnames.add("Eregnisfeld");
		fieldnames.add("Zusatzsteuer");
		fieldnames.add("Schlossallee");
		return fieldnames;
	}
	
	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}
	
	public ArrayList<Game> getGames() {
		return games;
	}
	
	public void addGame(Game game) {
		if(!games.contains(game)){
			games.add(game);	
		}
	}
	
	public void setField(ArrayList<Field> fields) {
		this.fields = fields;
	}
	
	public ArrayList<Field> getFields() {
		return fields;
	}
	
	public void setPositions(Map<String, Integer> positions) {
		this.positions = positions;
	}
	
	public Map<String, Integer> getPositions() {
		return positions;
	}
	
	public void setPlayer(ArrayList<Player> players) {
		this.players = players;
	}
	
	public void addPlayer(Player player) {
		if(!players.contains(player)) {
			players.add(player);
		}
		
	}
	
	public Player removePlayer(String playerid) {
		ArrayList<Player> copieList = new ArrayList<>(players);
		for(Player player : copieList) {
			if(player.getPlayerId().equals(playerid)) {
				players.remove(player);
				return player;
			}
		}
		return null;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public Player getPlayer(String playerid) {
		for(Player player : players) {
			if(player.getPlayerId().equals(playerid)) {
				return player;
			}
		}
		return null;
	}

	public void changePosition(Player player, int newPosition) {
		int previousPosition = player.getPosition();
		fields.get(previousPosition).removePlayer(player.getPlayerId());
		
		int position = newPosition % fields.size();
		player.setPosition(position);
		Field field = fields.get(position);
		field.addPlayer(player);
		player.setPlace(field.getPlace());
		
	}

}
