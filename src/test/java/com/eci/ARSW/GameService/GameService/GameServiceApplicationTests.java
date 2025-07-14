package com.eci.ARSW.GameService.GameService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.eci.ARSW.GameService.GameService.model.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameServiceApplicationTests {

	// Test cases for the GameService Application

	// Model Tests

	// Board Tests

	@Test
	void testBoardInitialization() {
		Board board = new Board(3, 4);
		assertEquals(3, board.getRows());
		assertEquals(4, board.getCols());
		assertNotNull(board.getMatrix());
		assertEquals(3, board.getMatrix().length);
		assertEquals(4, board.getMatrix()[0].length);
	}

	@Test
	void testBoardSetAndGetElementAt() {
		Board board = new Board(2, 2);
		Mine mine = new Mine(new Position(0, 0));
		board.setElementAt(1, 1, mine);
		assertEquals(mine, board.getElementAt(1, 1));
	}

	@Test
	void testBoardSetElementOutOfBounds() {
		Board board = new Board(2, 2);
		Player player = new Player(new Position(0, 0), 2);
		board.setElementAt(2, 2, player); // fuera de límites
		assertNull(board.getElementAt(2, 2));
	}

	@Test
	void testBoardRemoveElementAt() {
		Board board = new Board(2, 2);
		Tile tile = new Tile(1, 1);
		board.setElementAt(1, 1, tile);
		board.removeElementAt(1, 1);
		assertNull(board.getElementAt(1, 1));
	}

	@Test
	void testBoardIsEmpty() {
		Board board = new Board(2, 2);
		assertTrue(board.isEmpty(1, 1));
		Mine mine = new Mine(new Position(1, 1));
		board.setElementAt(1, 1, mine);
		assertFalse(board.isEmpty(1, 1));
	}

	@Test
	void testBoardIsInBounds() {
		Board board = new Board(2, 2);
		assertTrue(board.isInBounds(0, 0));
		assertTrue(board.isInBounds(1, 1));
		assertFalse(board.isInBounds(-1, 0));
		assertFalse(board.isInBounds(0, 2));
		assertFalse(board.isInBounds(2, 0));
	}

	// Mine Tests

	@Test
	void testMineCreation() {
		Position pos = new Position(2, 3);
		Mine mine = new Mine(pos);
		assertEquals(pos, mine.getPosition());
		assertEquals('E', mine.getState());
		assertEquals("ME", mine.getSymbol());
	}

	@Test
	void testMineSetState() {
		Mine mine = new Mine(new Position(0, 0));
		mine.setState('F');
		assertEquals('F', mine.getState());
		assertEquals("MF", mine.getSymbol());
	}

	@Test
	void testMineSymbol() {
		Mine mine = new Mine(new Position(0, 0));
		assertEquals("ME", mine.getSymbol());
		mine.setState('F');
		assertEquals("MF", mine.getSymbol());
	}

	@Test
	void testMineSetPosition() {
		Mine mine = new Mine(new Position(0, 0));
		Position newPos = new Position(5, 5);
		mine.setPosition(newPos);
		assertEquals(newPos, mine.getPosition());
	}

	// Tile Tests

	@Test
	void testTileInitialization() {
		Tile tile = new Tile(2, 3);
		assertEquals(2, tile.getPosition().getX());
		assertEquals(3, tile.getPosition().getY());
		assertFalse(tile.isRevealed());
		assertFalse(tile.isFlagged());
		assertEquals(0, tile.getAdjacentMines());
		assertEquals("T0N", tile.getSymbol());
	}

	@Test
	void testTileSetAndGetRevealed() {
		Tile tile = new Tile(0, 0);
		tile.setRevealed(true);
		assertTrue(tile.isRevealed());
		tile.setRevealed(false);
		assertFalse(tile.isRevealed());
	}

	@Test
	void testTileSetAndGetFlagged() {
		Tile tile = new Tile(1, 1);
		tile.setFlagged(true);
		assertTrue(tile.isFlagged());
		assertEquals("T0F", tile.getSymbol());
		tile.setFlagged(false);
		assertFalse(tile.isFlagged());
		assertEquals("T0N", tile.getSymbol());
	}

	@Test
	void testTileSetAndGetAdjacentMines() {
		Tile tile = new Tile(2, 2);
		tile.setAdjacentMines(3);
		assertEquals(3, tile.getAdjacentMines());
		assertEquals("T3N", tile.getSymbol());
	}

	@Test
	void testTileSetAndGetPosition() {
		Tile tile = new Tile(0, 0);
		Position newPos = new Position(5, 6);
		tile.setPosition(newPos);
		assertEquals(5, tile.getPosition().getX());
		assertEquals(6, tile.getPosition().getY());
	}

	// Player Tests

	@Test
	void testPlayerInitialization() {
		Position pos = new Position(2, 3);
		Player player = new Player(pos, 5);
		assertEquals(pos, player.getPosition());
		assertEquals(5, player.getMines());
		assertTrue(player.isState());
		assertEquals('N', player.getMode());
		assertEquals("P", player.getSymbol());
	}

	@Test
	void testPlayerSetAndGetId() {
		Player player = new Player();
		player.setId("abc123");
		assertEquals("abc123", player.getId());
	}

	@Test
	void testPlayerSetAndGetPosition() {
		Player player = new Player();
		Position pos = new Position(1, 1);
		player.setPosition(pos);
		assertEquals(pos, player.getPosition());
	}

	@Test
	void testPlayerSetAndGetMines() {
		Player player = new Player();
		player.setMines(7);
		assertEquals(7, player.getMines());
	}

	@Test
	void testPlayerSetAndGetState() {
		Player player = new Player();
		player.setState(false);
		assertFalse(player.isState());
		player.setState(true);
		assertTrue(player.isState());
	}

	@Test
	void testPlayerSetAndGetMode() {
		Player player = new Player();
		player.setMode('T');
		assertEquals('T', player.getMode());
		player.setMode('N');
		assertEquals('N', player.getMode());
	}

	@Test
	void testPlayerSetAndGetSymbol() {
		Player player = new Player();
		player.setSymbol("PX");
		assertEquals("PX", player.getSymbol());
	}

	// GameState Tests

	@Test
	void testDefaultConstructor() {
		GameState state = new GameState();
		assertNotNull(state.getCreatedAt());
		assertNotNull(state.getUpdatedAt());
		assertNull(state.getBoardMatrix());
		assertNull(state.getStatus());
		assertNull(state.getPlayers());
		assertNull(state.getMines());
	}

	@Test
	void testConstructorWithBoardMatrix() {
		String[][] matrix = {{"A", "B"}, {"C", "D"}};
		GameState state = new GameState(matrix);
		assertEquals(matrix, state.getBoardMatrix());
		assertEquals("IN_PROGRESS", state.getStatus());
		assertNotNull(state.getCreatedAt());
		assertNotNull(state.getUpdatedAt());
	}

	@Test
	void testSetAndGetBoardMatrix() {
		GameState state = new GameState();
		String[][] matrix = {{"X"}};
		state.setBoardMatrix(matrix);
		assertEquals(matrix, state.getBoardMatrix());
	}

	@Test
	void testSetAndGetStatus() {
		GameState state = new GameState();
		state.setStatus("FINISHED");
		assertEquals("FINISHED", state.getStatus());
	}

	@Test
	void testSetAndGetPlayers() {
		GameState state = new GameState();
		List<Player> players = Arrays.asList(new Player(), new Player());
		state.setPlayers(players);
		assertEquals(players, state.getPlayers());
	}

	@Test
	void testSetAndGetMines() {
		GameState state = new GameState();
		List<Mine> mines = Arrays.asList(new Mine(new Position()), new Mine(new Position()));
		state.setMines(mines);
		assertEquals(mines, state.getMines());
	}

	@Test
	void testSetAndGetCreatedAt() {
		GameState state = new GameState();
		Date date = new Date(123456789L);
		state.setCreatedAt(date);
		assertEquals(date, state.getCreatedAt());
	}

	@Test
	void testSetAndGetUpdatedAt() {
		GameState state = new GameState();
		Date date = new Date(987654321L);
		state.setUpdatedAt(date);
		assertEquals(date, state.getUpdatedAt());
	}


	@Test
	void testPositionSetters() {
		Position pos = new Position();
		pos.setX(5);
		pos.setY(6);
		assertEquals(5, pos.getX());
		assertEquals(6, pos.getY());
	}

		@Test
		void testGameManagerCreatePlayer() {
			GameManager manager = new GameManager(5, 5, 0, 2);
			Position pos = new Position(0, 0);
			Player player = manager.createPlayer(pos, 2);
			assertNotNull(player);
			assertEquals(pos.getX(), player.getPosition().getX());
			assertEquals(pos.getY(), player.getPosition().getY());
		}




}

