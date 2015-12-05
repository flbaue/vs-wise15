package de.haw_hamburg.vs.wise15.superteam.banks;

import com.google.gson.Gson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * Created by masha on 28.11.15.
 */
public class BankServiceTest {


    private Gson gson = new Gson();
    Bank bank;
    Game game;
    BankService bankService;
    Request request;
    Response response;
    @Before
    public void setUp() throws Exception {
        bankService = new BankService();
        ArrayList<Player> playerList = new ArrayList<Player>();
        playerList.add(new Player("player1", "name", "uri", new Place("place1"),1));
        game = new Game("game1",playerList,new Component("","","","","","",""));
        //
        request = mock(Request.class);

        response = mock(Response.class);



    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateBank() throws Exception {
        when(request.body()).thenReturn(gson.toJson("{gameId:23,playerList:[],component:{}}"));
        Assert.assertNotNull(bankService.createBank(request, response));
    }

    @Test
    public void testGetBank() throws Exception {
       // Assert.assertNotNull();
    }

}