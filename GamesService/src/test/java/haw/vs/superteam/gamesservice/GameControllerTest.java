package haw.vs.superteam.gamesservice;

import haw.vs.superteam.gamesservice.api.BoardsAdapter;
import haw.vs.superteam.gamesservice.api.PlayerAdapter;
import haw.vs.superteam.gamesservice.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by florian on 28.11.15.
 */
public class GameControllerTest {

    GameController controller;
    private Components components;
    private PlayerAdapter playerAdapter;
    private BoardsAdapter boardsAdapter;

    @Before
    public void setUp() throws Exception {
        components = new Components();
        boardsAdapter = mock(BoardsAdapter.class);
        when(boardsAdapter.createBoard(any(Game.class))).thenReturn(true);
        when(boardsAdapter.addPlayer(any(Game.class), any(Player.class))).thenReturn(true);
        playerAdapter = mock(PlayerAdapter.class);
        controller = new GameController("test-uri", playerAdapter, boardsAdapter);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetAll() throws Exception {

    }

    @Test
    public void testCreateNewGame() throws Exception {

        Game game1 = controller.createNewGame(components);
        assertNotNull(game1);

        Game game2 = controller.createNewGame(components);
        assertNotNull(game2);

        assertNotEquals(game1, game2);
        assertNotEquals(game1.getGameid(), game2.getGameid());

        assertNotNull(game1.getGameid());
        assertNotNull(game1.getComponents());
        assertNotNull(game1.getPlayers());
        assertEquals(game1.getPlayers().getPlayers().size(), 0);
        assertNotNull(game1.getUri());
        assertFalse(game1.isStarted());
    }

    @Test
    public void testGetGame() throws Exception {
        Game game1 = controller.createNewGame(components);

        Game game2 = controller.getGame(game1.getGameid());

        assertEquals(game1.getGameid(), game2.getGameid());
        assertEquals(game1, game2);
    }

    @Test
    public void testPlayerLifeCycle() throws Exception {

        String playerURI = "test-uri";
        String playerName = "Johnny Bravo";
        String playerID = "42";

        //Add
        Game game = controller.createNewGame(components);
        Boolean result = controller.addPlayerToGame(game.getGameid(), playerID, playerName, playerURI);
        assertTrue(result);

        //Get
        Player player = controller.getPlayerFromGame(game.getGameid(), playerID);
        assertNotNull(player);
        assertEquals(player.getUri(), playerURI);
        assertEquals(player.getName(), playerName);

        Player player1 = controller.getPlayerFromGame(game.getGameid(), "abc");
        assertNull(player1);

        Player player2 = controller.getPlayerFromGame("test", playerID);
        assertNull(player2);

        //Remove
        controller.removePlayerFromGame(game.getGameid(), playerID);
        Player player3 = controller.getPlayerFromGame(game.getGameid(), playerID);
        assertNull(player3);

    }

    @Test
    public void testPlayerReady() throws Exception {
        String playerURI = "test-uri";
        String playerName = "Johnny Bravo";
        String playerID = "42";

        //Add Player
        Game game = controller.createNewGame(components);
        Boolean result = controller.addPlayerToGame(game.getGameid(), playerID, playerName, playerURI);

        boolean ready1 = controller.isPlayerReady(game.getGameid(), playerID);
        assertFalse(ready1);
        controller.togglePlayerReady(game.getGameid(), playerID);
        boolean ready2 = controller.isPlayerReady(game.getGameid(), playerID);
        assertTrue(ready2);
        assertTrue(game.isStarted());

        verify(playerAdapter, times(1)).gameStart(game);
        assertNotEquals(ready1, ready2);
    }

    @Test
    public void testPlayerMutex() throws Exception {
        String playerURI = "test-uri";
        String playerName = "Johnny Bravo";
        String playerID = "42";
        Player player = new Player(playerID, playerName, playerURI);
        Player player2 = new Player("43", playerName, playerURI);

        //Add Player
        Game game = controller.createNewGame(components);
        controller.addPlayerToGame(game.getGameid(), playerID, playerName, playerURI);

        MutexStatus mutexStatus = controller.setMutex(game.getGameid(), player);
        assertEquals(mutexStatus, MutexStatus.SUCCESS);

        mutexStatus = controller.setMutex(game.getGameid(), player);
        assertEquals(mutexStatus, MutexStatus.ALREADY_HOLDING);

        mutexStatus = controller.setMutex(game.getGameid(), player2);
        assertEquals(mutexStatus, MutexStatus.FAILED);
    }
}