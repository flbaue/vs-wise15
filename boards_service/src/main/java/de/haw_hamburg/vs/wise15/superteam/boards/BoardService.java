package de.haw_hamburg.vs.wise15.superteam.boards;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by masha on 28.11.15.
 */
public class BoardService {

    private Gson gson = new Gson();
    private Board board;
    private Map<String, Board> boards = new HashMap<String, Board>();

    public static void main(String[] args) {
        new BoardService().run();
    }
    
    public BoardService() {
    	
    	registreService();
    }

    private void run() {
        System.out.println("BoardService is starting");
        
        //returns all active games (both running and joinable)
        get("/boards", this::getBoard);
        
        //gets the board belonging to the game
        get("/boards/:gameid", this::getBoardForGame);
        
        //makes sure there is a board for the gameid
        put("/boards/:gameid", this::putBoardForGame);
        
        
        //deletes the board to the game, effectivly ending the game
        delete("/boards/:gameid", this::deleteBoardForGame);
        
        //returns a list of all player positions
        get("/boards/:gameid/players", this::getPlayerPositions);
        
        //places a players
        put("/boards/:gameid/players/:playerid", this::setPlayer);
        
        //removes a player from the board
        delete("/boards/:gameid/players/:playerid", this::removePlayer);
        
        //Gets a players
        get("/boards/:gameid/players/:playerid", this::getPlayer);
        
        //gives a throw of dice from the player to the board
        post("/boards/:gameid/players/:playerid/roll", this::rollDiceForPlayer);
        
        //List of available places
        get("/boards/:gameid/places", this::getAllPlaces);
        
        //Gets a places
        get("/boards/:gameid/places/:place", this::getPlace);
        
        //places a places
        put("/boards/:gameid/places/:place", this::setPlace);
    }
    

	private Object getBoard(Request request, Response respone) {
    	String result = gson.toJson(boards);
    	respone.status(200);
		return result;
    }
	
	private Object getBoardForGame(Request request, Response response) {
		String gameid = request.params(":gameid");
		board = boards.get(gameid);
		String result = gson.toJson(board);
		response.status(200);
		return result;
	}
	
	private Object putBoardForGame(Request request, Response response) {
		String gameid = request.params(":gameid");
		String boardJson = request.body();
		Board board = gson.fromJson(boardJson, Board.class);
		if(board == null){
			board = new Board();
		}
		boards.put(gameid, board);
		
		/*createBroker(gameid);
		for(Map.Entry<String, Board> entry : boards.entrySet()) {
			
			for(Field field : entry.getValue().getFields()) {
				registerProperties(entry.getKey(), field.getPlace().getPlaceid()); //TODO
			}
			
		}*/
		
		return "";
	}
	
	private Object deleteBoardForGame(Request request, Response response) {
		String gameid = request.params(":gameid");
		boards.remove(gameid);
		return null;
	}
	
	private Object getPlayerPositions(Request request, Response response) {
		String gameid = request.params(":gameid");
		board = boards.get(gameid);
		
		/*ArrayList<Player> players = new ArrayList<Player>();
		for(Game game : board.getGames()) {
			if(game.getGameId().equals(gameid)) {
				players = game.getPlayerList();
			}
		}*/
		String result = gson.toJson(board.getPlayers());
		response.status(200);
		return result;
	}
	
	private Object setPlayer(Request request, Response response) {
		String gameid = request.params(":gameid");
		String playerid = request.params(":playerid");
		String playerJson = request.body();
		Player player = gson.fromJson(playerJson, Player.class);
		board = boards.get(gameid);
		if(player == null) {
			player = new Player(playerid, "", "", null, 0);
		}
		player.setPlayerId(playerid);
		board.addPlayer(player);
		return gson.toJson(player);
	}
	
	private Object removePlayer(Request request, Response response) {
		String gameid = request.params(":gameid");
		String playerid = request.params("playerid");
		board = boards.get(gameid);
		board.removePlayer(playerid);
		return "";
	}
	
	private Object getPlayer(Request request, Response response) {
		String gameid = request.params(":gameid");
		String playerid = request.params(":playerid");
		board  = boards.get(gameid);
		ArrayList<Player>players = board.getPlayers();
		Player player = null;
		for(Player p : players) {
			if(p.getPlayerId().equals(playerid)) {
				player = p;
			}
		}
		response.status(200);
		return gson.toJson(player);
	}
	
	private Object rollDiceForPlayer(Request request, Response response) {
		String gameid = request.params(":gameid");
		String playerid = request.params(":playerid");
		Throw playerThrow = gson.fromJson(request.body(), Throw.class);
		//TODO gameid not found
		board = boards.get(gameid);
		Player player = board.getPlayer(playerid);
		
		if(playerThrow == null) {
			playerThrow = new Throw();
		}
		
		int rolledNumber = playerThrow.getRoll1().getNumber() + playerThrow.getRoll2().getNumber();
		
		board.changePosition(player, player.getPosition() + rolledNumber);
		
		//registerVisitor(gameid, player.getPlace().getPlaceid(), playerid);
		
		return gson.toJson(new BoardState(player, board));
	}
	
	private Object getAllPlaces(Request request, Response response) {
		String gameid =request.params(":gameid");
		board = boards.get(gameid);
		ArrayList<Field> fields = board.getFields();
		ArrayList<Place> places = new ArrayList<Place>();
		for(Field field : fields) {
			places.add(field.getPlace());
		}
		response.status(200);
		return gson.toJson(places);
	}
	
	private Object getPlace(Request request, Response response) {
		String gameid =request.params(":gameid");
		String placename = request.params(":place");
		board = boards.get(gameid);
		Place place = null;
		ArrayList<Field> fields = board.getFields();
		for(Field f : fields) {
			if(f.getPlace().getName().equals(placename)) {
				place = f.getPlace();
			}
		}
		response.status(200);
		return gson.toJson(place);
	}
	
	private Object setPlace(Request request, Response response) {
		String gameid = request.params(":gameid");
		int placeposition = Integer.parseInt(request.params(":place"));
		Place place = gson.fromJson(request.body(), Place.class);
		
		if(place == null) {
			place = new Place("toller platz");
		}
		
		registerProperties(gameid, place.getPlaceid()); //TODO
		
		board = boards.get(gameid);
		ArrayList<Field> fields = board.getFields();
		if(placeposition < fields.size()) {
			fields.get(placeposition).setPlace(place);
		}else{
			Field field = new Field();
			field.setPlace(place);
			fields.add(field);
		}
		return "";
	}
	
	private void createBroker(String gameid) {
		/*try {
			HttpResponse<JsonNode> jsonResponse = Unirest.put("https://vs-docker.informatik.haw-hamburg.de:port/brokers/{gameid}")
					.routeParam("gameid", gameid)
					.asJson();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private void registerProperties(String gameid, String placeid) {
		/*try {
			HttpResponse<JsonNode> jsonResponse = Unirest.put("https://vs-docker.informatik.haw-hamburg.de:port/brokers/{gameid}/places/{placeid}")
					.routeParam("gameid", gameid)
					.routeParam("placeid", placeid)
					.asJson();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private void registerVisitor(String gameid, String placeid, String playerid)  {
		/*try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post("https://vs-docker.informatik.haw-hamburg.de:port/brokers/{gameid}/places/{placeid}/visit/{placerid}")
					.routeParam("gameid", gameid)
					.routeParam("placeid", placeid)
					.routeParam("playerid", playerid)
					.asJson();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
	private void registreService() {
		
	}
}



