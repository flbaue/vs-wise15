package de.haw_hamburg.vs.wise15.superteam.boards;

import java.util.ArrayList;

public class Field {
	
	private Place place;
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public void setPlace(Place place) {
		this.place = place;
	}
	
	public Place getPlace() {
		return place;
	}
	
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}

	public void removePlayer(String playerid) {
		for(Player player : players) {
			if(player.getPlayerId().equals(playerid)) {
				players.remove(player);
			}
		}
		
	}

}
