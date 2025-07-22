package com.eci.ARSW.GameService.GameService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.eci.ARSW.GameService.GameService.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import com.eci.ARSW.GameService.GameService.repository.GameStateRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameServiceApplicationTests {


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
		assertTrue(tile.isRevealed());
		assertFalse(tile.isFlagged());
		assertEquals(0, tile.getAdjacentMines());
		assertEquals("T0NR", tile.getSymbol());
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
		assertEquals("T0FR", tile.getSymbol());
		tile.setFlagged(false);
		assertFalse(tile.isFlagged());
		assertEquals("T0NR", tile.getSymbol());
	}

	@Test
	void testTileSetAndGetAdjacentMines() {
		Tile tile = new Tile(2, 2);
		tile.setAdjacentMines(3);
		assertEquals(3, tile.getAdjacentMines());
		assertEquals("T3NR", tile.getSymbol());
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


	// GameManager Tests

	@Test
	void testGameManagerMoveRightPlayer() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Position startPos = new Position(0, 0);
		Player player = manager.createPlayer(startPos, 2);
		player.setPosition(startPos);
		manager.getBoard().setElementAt(0,0, player);
		assertNotNull(player);
		Position newPos = new Position(0, 1);
		manager.movePlayer("P1", 'D');
		assertEquals(newPos.getX(), player.getPosition().getX());
		assertEquals(newPos.getY(), player.getPosition().getY());
		assertEquals("P1", manager.getBoard().getElementAt(player.getPosition().getX(), player.getPosition().getY()).getSymbol());
	}

	@Test
	void testGameManagerMoveLeftPlayer() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Position startPos = new Position(1, 1);
		Player player = manager.createPlayer(startPos, 2);
		player.setPosition(startPos);
		assertNotNull(player);
		Position newPos = new Position(1, 0);
		manager.movePlayer("P1", 'A');
		assertEquals(newPos.getX(), player.getPosition().getX());
		assertEquals(newPos.getY(), player.getPosition().getY());
		assertEquals("P1", manager.getBoard().getElementAt(player.getPosition().getX(), player.getPosition().getY()).getSymbol());

	}

	@Test
	void testGameManagerMoveDownPlayer() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Position startPos = new Position(1, 1);
		Player player = manager.createPlayer(startPos, 2);
		player.setPosition(startPos);
		manager.getBoard().setElementAt(1, 1, player);
		manager.getBoard().setElementAt(2, 1, new Tile(2, 1));
		assertNotNull(player);
		Position newPos = new Position(2, 1);
		manager.movePlayer("P1", 'S');
		assertEquals(newPos.getX(), player.getPosition().getX());
		assertEquals(newPos.getY(), player.getPosition().getY());
		assertEquals("P1", manager.getBoard().getElementAt(player.getPosition().getX(), player.getPosition().getY()).getSymbol());

	}

	@Test
	void testGameManagerMoveUpPlayer() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Position startPos = new Position(1, 1);
		Player player = manager.createPlayer(startPos, 2);
		player.setPosition(startPos);
		manager.getBoard().setElementAt(1, 1, player);
		manager.getBoard().setElementAt(0, 1, new Tile(0, 1));
		assertNotNull(player);
		Position newPos = new Position(0, 1);
		manager.movePlayer("P1", 'W');
		assertEquals(newPos.getX(), player.getPosition().getX());
		assertEquals(newPos.getY(), player.getPosition().getY());
		assertEquals("P1", manager.getBoard().getElementAt(player.getPosition().getX(), player.getPosition().getY()).getSymbol());

	}

	@Test
	void testMoveOutOfBoundsFails() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		manager.createPlayer(new Position(0, 0), 2);
		Player player = manager.getPlayers().get("P1");
		player.setPosition(new Position(0, 0));
		manager.getBoard().setElementAt(0, 0, player);
		boolean result = manager.movePlayer("P1", 'W'); // fuera del tablero
		assertFalse(result);
		assertEquals(0, manager.getPlayers().get("P1").getPosition().getX());
		assertEquals(0, manager.getPlayers().get("P1").getPosition().getY());
	}

	@Test
	void testMoveInvalidDirectionFails() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		manager.createPlayer(new Position(1, 1), 2);
		boolean result = manager.movePlayer("P1", 'Z');
		assertFalse(result);
	}

	@Test
	void testMoveIntoOtherPlayerFails() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		manager.createPlayer(new Position(1, 1), 2);
		Player p1 = manager.getPlayers().get("P1");
		manager.getBoard().setElementAt(1, 1, p1);
		p1.setPosition(new Position(1, 1));
		manager.createPlayer(new Position(1, 2), 2);
		Player p2 = manager.getPlayers().get("P2");
		p2.setPosition(new Position(1, 2));
		manager.getBoard().setElementAt(1, 2, p2);

		boolean result = manager.movePlayer("P1", 'D');
		assertFalse(result);
		assertEquals(1, manager.getPlayers().get("P1").getPosition().getX());
		assertEquals(1, manager.getPlayers().get("P1").getPosition().getY());
	}

	@Test
	void testMoveIntoMineKillsPlayer() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		manager.createPlayer(new Position(1, 1), 2);
		Position pos = new Position(1, 1);
		Player player = manager.getPlayers().get("P1");
		player.setPosition(pos);
		manager.getBoard().setElementAt(1, 2, new Mine(new Position(1, 2))); // mina a la derecha

		boolean result = manager.movePlayer("P1", 'D');
		assertTrue(result);

		assertFalse(player.isState());

		GameElement elem = manager.getBoard().getElementAt(1, 2);
		assertTrue(elem instanceof Mine);
		assertEquals('D', ((Mine) elem).getState());
	}

	@Test
	void testMoveRestoresPreviousTile() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		manager.createPlayer(new Position(1, 1), 2);
		Position pos = new Position(1, 1);
		Player player = manager.getPlayers().get("P1");
		player.setPosition(pos);
		manager.getBoard().setElementAt(1, 1, player);
		manager.getBoard().setElementAt(1, 2, new Tile(1, 2));

		// Verifica que antes haya un Tile vacío en (1,1)
		GameElement initial = manager.getBoard().getElementAt(1, 1);
		assertTrue(initial instanceof Player);

		manager.movePlayer("P1", 'D');

		GameElement restored = manager.getBoard().getElementAt(1, 1);
		assertTrue(restored instanceof Tile); // la casilla anterior ahora es un Tile
	}


	@Test
	void testPlaceMineSuccessfully() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Position startPos = new Position(2, 2);
		Player player = manager.createPlayer(startPos, 2);
		player.setMode('T');
		player.setPosition(startPos);
		manager.getBoard().setElementAt(2, 2, player);
		manager.getBoard().setElementAt(1, 2, new Tile(1, 2));

		manager.placeMine(player.getSymbol(), 'u');

		GameElement element = manager.getBoard().getElementAt(1, 2);
		assertTrue(element instanceof Mine);
		Mine mine = (Mine) element;
		assertEquals('E', mine.getState());
	}

	@Test
	void testPlaceMineFailsIfPlayerIsNull() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		// Jugador inexistente
		manager.placeMine("P99", 'u');

		GameElement element = manager.getBoard().getElementAt(1, 2);
		assertFalse(element instanceof Mine);
	}

	@Test
	void testPlaceMineFailsIfPlayerIsDead() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Position startPos = new Position(1, 1);
		Player player = manager.createPlayer(startPos, 2);
		player.setMode('T');
		player.setState(false); // Jugador muerto

		manager.placeMine(player.getSymbol(), 'u');

		GameElement element = manager.getBoard().getElementAt(0, 1);
		assertFalse(element instanceof Mine);
	}

	@Test
	void testPlaceMineFailsIfPlayerNotInTacticalMode() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Position startPos = new Position(3, 3);
		Player player = manager.createPlayer(startPos, 2);
		// Modo por defecto: 'N'

		manager.placeMine(player.getSymbol(), 'u');

		GameElement element = manager.getBoard().getElementAt(2, 3);
		assertFalse(element instanceof Mine);
	}

	@Test
	void testFlagEnabledMine() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Player player = manager.createPlayer(new Position(1, 1), 2);
		player.setPosition(new Position(1, 1));
		player.setMode('T');
		manager.getBoard().setElementAt(1, 1, player);

		manager.getBoard().setElementAt(0, 1, new Mine(new Position(0, 1)));


		GameElement element = manager.getBoard().getElementAt(0, 1);
		assertTrue(element instanceof Mine);
		Mine mine = (Mine) element;

		assertEquals('E', mine.getState());
		manager.flagElement(player.getSymbol(), 'u');
		assertEquals('F', mine.getState());
	}

	@Test
	void testFlagTileSuccessfully() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Player player = manager.createPlayer(new Position(1, 1), 2);
		Position positionPlayer = new Position(1, 1);
		player.setPosition(positionPlayer);
		player.setMode('T');
		manager.getBoard().setElementAt(1, 1, player);
		manager.getBoard().setElementAt(0, 1, new Tile(0, 1));
		manager.placeMine(player.getSymbol(), 'u');

		GameElement elem = manager.getBoard().getElementAt(1, 0 );
		assertTrue(elem instanceof Tile);
		Tile tile = (Tile) elem;
		assertFalse(tile.isFlagged());

		manager.flagElement(player.getSymbol(), 'l');
		assertTrue(tile.isFlagged());
	}


	@Test
	void testFlagFailsIfPlayerIsDead() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Player player = manager.createPlayer(new Position(2, 2), 2);
		player.setMode('T');
		player.setState(false);
		player.setPosition(new Position(2, 2));
		manager.getBoard().setElementAt(2, 2, player);
		manager.getBoard().setElementAt(1, 2, new Tile(1, 2));
		String symbol = player.getSymbol();
		Position pos = new Position(2, 2);
		player.setPosition(pos);
		manager.getBoard().setElementAt(2, 2, player);

		manager.placeMine(symbol, 'u');
		assertInstanceOf(Tile.class, manager.getBoard().getElementAt(1, 2));
	}

	@Test
	void testFlagFailsIfPlayerDoesNotExist() {
		GameManager manager = new GameManager(5, 5, 0, 2);

		// Tile por defecto
		GameElement elem = manager.getBoard().getElementAt(0, 0);
		assertTrue(elem instanceof Tile);
		Tile tile = (Tile) elem;
		assertFalse(tile.isFlagged());

		manager.flagElement("P404", 'r');
		assertFalse(tile.isFlagged());
	}

	@Test
	void testUnflagFlaggedMine() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Player player = manager.createPlayer(new Position(1, 1), 2);
		player.setMode('T');
		String symbol = player.getSymbol();
		player.setPosition(new Position(1, 1));
		manager.getBoard().setElementAt(1, 1, player);
		manager.getBoard().setElementAt(1, 2, new Tile(1,2));

		manager.placeMine(symbol, 'r');
		manager.placeMine(symbol, 'u');

		// Flag the mine first
		manager.flagElement(symbol, 'r');
		GameElement flagged = manager.getBoard().getElementAt(1, 2);
		assertTrue(flagged instanceof Mine);
		assertEquals('F', ((Mine) flagged).getState());

		// Unflag it
		manager.flagElement(symbol, 'r');
		assertEquals('E', ((Mine) flagged).getState());
	}

	@Test
	void testUnflagFlaggedTile() {
		GameManager manager = new GameManager(5, 5, 2, 2);
		Player player = manager.createPlayer(new Position(0, 0), 2);
		player.setPosition(new Position(0, 0));
		manager.getBoard().setElementAt(0, 0, player);
		manager.getBoard().setElementAt(0, 1, new Tile(0, 1));

		String symbol = player.getSymbol();
		manager.flagElement(symbol, 'r');
		manager.flagElement(symbol, 'r');
		Tile tile = (Tile) manager.getBoard().getElementAt(0, 1);
		assertFalse(tile.isFlagged());
	}


	@Test
	void testUnflagFailsIfPlayerDead() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Player player = manager.createPlayer(new Position(0, 0), 2);
		player.setMode('T');
		player.setPosition(new Position(0, 0));
		manager.getBoard().setElementAt(0, 0, player);
		manager.getBoard().setElementAt(0, 1, new Tile(0,1));
		String symbol = player.getSymbol();
		manager.placeMine(symbol, 'r');
		manager.flagElement(symbol, 'r');

		player.setState(false);
		manager.flagElement(symbol, 'r');

		assertEquals('F', ((Mine) manager.getBoard().getElementAt(0, 1)).getState());
	}

	@Test
	void testBoardHasCorrectNumberOfMinesAfterInit() {
		GameManager manager = new GameManager(5, 5, 5, 2); // 5 minas esperadas
		int mineCount = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (manager.getBoard().getElementAt(i, j) instanceof Mine) {
					mineCount++;
				}
			}
		}
		assertEquals(5, mineCount);
	}


	@Test
	void testBoardHasCorrectNumberOfHiddenTiles() {
		GameManager manager = new GameManager(5, 5, 5, 2); // 5 minas esperadas
		int hiddenTiles = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (manager.getBoard().getElementAt(i, j) instanceof Tile tile) {
					if (!tile.isRevealed()) {
						hiddenTiles++;
					}
				}
			}
		}
		assertEquals(10, hiddenTiles);
	}



	@Test
		void testGameManagerCreatePlayer() {
			GameManager manager = new GameManager(5, 5, 0, 2);
			Position pos = new Position(0, 0);
			Player player = manager.createPlayer(pos, 2);
			player.setPosition(pos);
			assertNotNull(player);
			assertEquals(pos.getX(), player.getPosition().getX());
			assertEquals(pos.getY(), player.getPosition().getY());
		}

	@Test
	void testGetMinesReturnsPlacedMines() {
		GameManager manager = new GameManager(5, 5, 0, 2); // sin minas al inicio
		Player player = manager.createPlayer(new Position(0, 0), 2);
		Position startPos = new Position(0, 0);
		player.setPosition(startPos);
		player.setMode('T');
		manager.getBoard().setElementAt(0, 0, player);
		manager.getBoard().setElementAt(0, 1, new Tile(0, 1));
		manager.getBoard().setElementAt(1, 0, new Tile(1, 0));

		manager.placeMine(player.getSymbol(), 'r');
		manager.placeMine(player.getSymbol(), 'd');

		List<Mine> mines = manager.getMines();
		assertEquals(2, mines.size());
		assertTrue(mines.stream().anyMatch(m -> m.getPosition().getX() == 0 && m.getPosition().getY() == 1));
		assertTrue(mines.stream().anyMatch(m -> m.getPosition().getX() == 1 && m.getPosition().getY() == 0));
	}

	@Test
	void testGetGameStateReturnsUpdatedBoardMatrix() {
		GameManager manager = new GameManager(4, 4, 0, 2);
		Player player = manager.createPlayer(new Position(0, 0), 2);
		Position startPos = new Position(0, 0);
		player.setPosition(startPos);
		player.setMode('T');
		manager.getBoard().setElementAt(0, 0, player);
		manager.getBoard().setElementAt(0, 1, new Tile(0, 1));
		manager.placeMine(player.getSymbol(), 'r');

		GameState state = manager.getGameState();
		String[][] matrix = state.getBoardMatrix();

		assertNotNull(matrix);
		assertEquals("ME", matrix[0][1]);
		assertEquals("P1", matrix[0][0]);
		assertEquals("IN_PROGRESS", state.getStatus());
	}

	@Test
	public void testLoadFromMatrixRestoresMinesPlayersAndTiles() {
		int rows = 3, cols = 3;
		String[][] matrix = {
				{"ME", "T1N", "P1"},
				{"T2F", "T2F",  "T1N"},
				{"T1N", "MF", "T1N"}
		};

		// 1. Crear jugadores
		Player p1 = new Player(new Position(0, 2), 2);
		p1.setSymbol("P1");
		p1.setMode('N');
		p1.setState(true);

		List<Player> players = List.of(p1);

		// 2. Crear minas
		Mine m1 = new Mine(new Position(0, 0));
		m1.setState('E');

		Mine m2 = new Mine(new Position(2, 1));
		m2.setState('F');

		List<Mine> mines = List.of(m1, m2);

		// 3. Crear GameState simulado
		GameState gameState = new GameState(matrix);
		gameState.setPlayers(players);
		gameState.setMines(mines);
		gameState.setStatus("IN_PROGRESS");

		// 4. Cargar el GameState en un GameManager
		GameManager manager = new GameManager();
		manager.loadFromMatrix(gameState);

		// 5. Validar minas
		assertEquals(2, manager.getMines().size());
		assertEquals('E', manager.getMines().get(0).getState());
		assertEquals('F', manager.getMines().get(1).getState());
		assertEquals("ME", ((Mine) manager.getBoard().getElementAt(0, 0)).getSymbol());
		assertEquals("MF", ((Mine) manager.getBoard().getElementAt(2, 1)).getSymbol());


		// 6. Validar jugador
		assertEquals(1, manager.getPlayers().size());
		Player loaded = manager.getPlayers().get("P1");
		assertNotNull(loaded);
		assertEquals(0, loaded.getPosition().getX());
		assertEquals(2, loaded.getPosition().getY());
		assertTrue(loaded.isState());
		assertEquals('N', loaded.getMode());
		assertEquals("P1", ((Player) manager.getBoard().getElementAt(0, 2)).getSymbol());

		// 7. Validar Tile con bandera
		GameElement flagged = manager.getBoard().getElementAt(1, 0);
		assertInstanceOf(Tile.class, flagged);
		assertTrue(((Tile) flagged).isFlagged());
		assertEquals("T2FR", ((Tile) flagged).getSymbol());

		// 8. Validar símbolo exacto
		assertEquals("T1NR", ((Tile) manager.getBoard().getElementAt(2, 0)).getSymbol());

		// 9. Validar estado general
		assertEquals("IN_PROGRESS", manager.getGameState().getStatus());
	}


	// Pruebas para changePlayerMode
	@Test
	void testChangePlayerModeToTactical() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Player player = manager.createPlayer(new Position(1, 1), 2);
		manager.changePlayerMode(player.getSymbol(), 'T');
		assertEquals('T', player.getMode());
	}

	@Test
	void testChangePlayerModeToNormal() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Player player = manager.createPlayer(new Position(1, 1), 2);
		player.setMode('T');
		manager.changePlayerMode(player.getSymbol(), 'N');
		assertEquals('N', player.getMode());
	}

	@Test
	void testChangePlayerModeFailsForInvalidMode() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Player player = manager.createPlayer(new Position(1, 1), 2);
		assertThrows(IllegalArgumentException.class, () -> {
			manager.changePlayerMode(player.getSymbol(), 'X');
		});
	}

	@Test
	void testChangePlayerModeFailsForDeadPlayer() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Player player = manager.createPlayer(new Position(1, 1), 2);
		player.setState(false);
		manager.changePlayerMode(player.getSymbol(), 'T');
		assertNotEquals('T', player.getMode());
	}


	@Test
	void testGameEndsWhenAllPlayersDead() {
		GameManager manager = new GameManager(2, 2, 0, 1);
		Player player = manager.createPlayer(new Position(0, 0), 1);
		player.setPosition(new Position(0, 0));
		player.setMode('T');
		manager.getBoard().setElementAt(0,0, player);
		manager.getBoard().setElementAt(0, 1, new Tile(0,1));
		// Matar al jugador
		manager.placeMine(player.getId(), 'r');
		manager.movePlayer(player.getSymbol(), 'D');
		assertEquals(false, player.isState());
		assertEquals("FINISHED", manager.getGameState().getStatus());
	}

	// Pruebas para placeMine en casos límite
	@Test
	void testPlaceMineFailsWhenPositionOccupied() {
		GameManager manager = new GameManager(5, 5, 0, 2);
		Player player1 = manager.createPlayer(new Position(1, 1), 2);
		player1.setPosition(new Position(1, 1));
		manager.getBoard().setElementAt(1, 1, player1);
		Player player2 = manager.createPlayer(new Position(1, 2), 2);
		player2.setPosition(new Position(1, 2));
		manager.getBoard().setElementAt(1, 2, player2);
		player1.setMode('T');

		// Intentar colocar mina donde está otro jugador
		manager.placeMine(player1.getSymbol(), 'r');

		GameElement element = manager.getBoard().getElementAt(1, 2);
		assertFalse(element instanceof Mine);
	}

	@Test
	void testPlaceMineFailsWhenGameFinished() {
		GameManager manager = new GameManager(2, 2, 1, 1);
		Player player = manager.createPlayer(new Position(0, 0), 1);
		player.setPosition(new Position(0, 0));
		manager.getBoard().setElementAt(0,0, player);
		player.setMode('T');

		// Terminar el juego
		manager.getBoard().setElementAt(0, 1, new Mine(new Position(0, 1)));
		manager.movePlayer(player.getSymbol(), 'D');

		// Intentar colocar mina
		manager.placeMine(player.getSymbol(), 'r');
		GameElement element = manager.getBoard().getElementAt(0, 1);
		assertNotEquals('E', ((Mine) element).getState());
	}


}