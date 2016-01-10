package de.haw_hamburg.vs.wise15.superteam.boards;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import spark.Request;
import spark.Response;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

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
    	
    	//registreService();
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
        get("/boards/:gameid/players", this::getPlayers);
        
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
		
		if(!boards.containsKey(gameid)) {
			response.status(400);
			return "no board found for game with id " + gameid;
		}
		
		board = boards.get(gameid);
		String result = gson.toJson(board);
		response.status(200);
		return result;
	}
	
	private Object putBoardForGame(Request request, Response response) {
		String gameid = request.params(":gameid");
		String gameJson = request.body();
		Game game = gson.fromJson(gameJson, Game.class);
		
		/*if(game == null) {
			response.status(400);
			return "bad request";
		}*/
		
		Board board = new Board();
		board.initializeBoard();
		boards.put(gameid, board);
		board.addGame(game);
		
		//Erstelle Broker
		createBroker(gameid);
		
		//Registriere alle verfügbaren Grundstücke beim Broker
		for(Map.Entry<String, Board> entry : boards.entrySet()) {
			for(int i = 0; i < entry.getValue().getFields().size(); i++) {
				registerProperties(entry.getKey(), i);
			}
			
		}
		return "";
	}
	
	private Object deleteBoardForGame(Request request, Response response) {
		String gameid = request.params(":gameid");
		
		if(!boards.containsKey(gameid)) {
			response.status(400);
			return "no board found for game with id " + gameid;
		}
		
		boards.remove(gameid);
		return "";
	}
	
	private Object getPlayers(Request request, Response response) {
		String gameid = request.params(":gameid");
		board = boards.get(gameid);
		
		if(board == null) {
			response.status(400);
			return "no board found for game with id " + gameid;
		}
		
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
		
		if(board == null) {
			response.status(400);
			return "no board found for game with id " + gameid;
		}else if(player == null) {
			//player = new Player(playerid, "", "", null, 0);
			response.status(400);
			return "bad request";
		}
		player.setPlayerId(playerid);
		board.addPlayer(player);
		board.changePosition(player, player.getPosition());
		return gson.toJson(player);
	}
	
	private Object removePlayer(Request request, Response response) {
		String gameid = request.params(":gameid");
		String playerid = request.params("playerid");
		board = boards.get(gameid);
		ArrayList<Player> players = board.getPlayers();
		
		if(board == null) {
			response.status(400);
			return "no board found for game with id " + gameid;
		}else if(board.removePlayer(playerid) == null) {
			response.status(400);
			return "no player found with id " + playerid;
		}
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
		
		if(player == null) {
			response.status(400);
			return "no player found with id " + playerid;
		}else if(board == null) {
			response.status(400);
			return "no board found for game with id " + gameid;
		}
		
		response.status(200);
		return gson.toJson(player);
	}
	
	private Object rollDiceForPlayer(Request request, Response response) {
		String gameid = request.params(":gameid");
		String playerid = request.params(":playerid");
		Throw playerThrow = gson.fromJson(request.body(), Throw.class);
		board = boards.get(gameid);
		Player player = board.getPlayer(playerid);
		
		if(playerThrow == null) {
			response.status(400);
			return "bad request";
		}else if(player == null) {
			response.status(400);
			return "no player found with id " + playerid;
		}else if(board == null) {
			response.status(400);
			return "no board found for game with id " + gameid;
		}
		
		int rolledNumber = playerThrow.getRoll1().getNumber() + playerThrow.getRoll2().getNumber();
		
		board.changePosition(player, player.getPosition() + rolledNumber);
		
		registerVisitor(gameid, board.getPlayer(playerid).getPosition(), playerid);
		
		return gson.toJson(new BoardState(player, board));
	}
	
	private Object getAllPlaces(Request request, Response response) {
		String gameid = request.params(":gameid");
		
		if(boards.containsKey(gameid)) {
			response.status(400);
			return "no board found for game with id " + gameid;
		}
		
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
		String gameid = request.params(":gameid");
		int placenumber = Integer.parseInt(request.params(":place"));
		
		if(!boards.containsKey(gameid)) {
			response.status(400);
			return "no board foundfor game with id " + gameid;
		}
		
		board = boards.get(gameid);
		ArrayList<Field> fields = board.getFields();
		Place place = fields.get(placenumber).getPlace();
		
		if(place == null) {
			response.status(400);
			return "no place found with id " + placenumber;
		}
		
		response.status(200);
		return gson.toJson(place);
	}
	
	private Object setPlace(Request request, Response response) {
		String gameid = request.params(":gameid");
		int placeposition = Integer.parseInt(request.params(":place"));
		Place place = gson.fromJson(request.body(), Place.class);
		if(place == null) {
			response.status(400);
			return "bad request";
		}else if(!boards.containsKey(gameid)) {
			response.status(400);
			return "no board found for game with id " + gameid;
		}
		
		registerProperties(gameid, placeposition);
		
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
			e.printStackTrace();
		}*/
	}
	
	private void registerProperties(String gameid, int placeid) {
		/*try {
			HttpResponse<JsonNode> jsonResponse = Unirest.put("https://vs-docker.informatik.haw-hamburg.de:port/brokers/{gameid}/places/{placeid}")
					.routeParam("gameid", gameid)
					.routeParam("placeid", String.valueOf(placeid))
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}*/
	}
	
	private void registerVisitor(String gameid, int placeid, String playerid)  {
		/*try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post("https://vs-docker.informatik.haw-hamburg.de:port/brokers/{gameid}/places/{placeid}/visit/{placerid}")
					.routeParam("gameid", gameid)
					.routeParam("placeid", String.valueOf(placeid))
					.routeParam("playerid", playerid)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}*/
		
	}
	
	private void registreService() {
		try{
			String ip = InetAddress.getLocalHost().getHostAddress();
			String uri = "https://vs-docker.informatik.haw-hamburg.de/cnt/"+ ip +"/4567";
			String body = "{"
				  +"\"name\": \"SuperTeamBoardsService\","
				  +"\"description\": \"Superteams Boardservice\","
				  +"\"service\": \"boards\","
				  +"\"uri\": \""+ uri +"\""
				+"}";
			
			System.out.println(body);
			SSLContext sslcontext;
				sslcontext = SSLContexts.custom()
		   	      .loadTrustMaterial(null, new TrustSelfSignedStrategy())
			      .build();
			
	
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
			CloseableHttpClient httpclient = HttpClients.custom()
	                         .setSSLSocketFactory(sslsf)
	                         .build();
			Unirest.setHttpClient(httpclient);
			
			HttpResponse<String> jsonResponse = Unirest.post("https://vs-docker.informatik.haw-hamburg.de/ports/8053/services")
			        .header("Content-Type", "application/json")
			        .body(body)
			        .asString();
			System.out.println(jsonResponse.getStatus());
			System.out.println(jsonResponse.getStatusText());
		} catch (UnirestException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}
}



