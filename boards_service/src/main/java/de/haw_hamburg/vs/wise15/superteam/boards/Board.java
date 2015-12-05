package de.haw_hamburg.vs.wise15.superteam.boards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
	
	private ArrayList<Field> fields = new ArrayList<Field>();
	private Map<String, Integer> positions = new HashMap<String, Integer>();
	private ArrayList<Game> games = new ArrayList<Game>();
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}
	
	public ArrayList<Game> getGames() {
		return games;
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
		players.add(player);
	}
	
	public void removePlayer(Player player) {
		players.remove(player);
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
		fields.get(position).addPlayer(player);
		
	}

}
