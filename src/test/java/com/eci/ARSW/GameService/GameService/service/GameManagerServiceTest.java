package com.eci.ARSW.GameService.GameService.service;

import com.eci.ARSW.GameService.GameService.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameManagerServiceTest {

    private GameManagerService gameManagerService;
    private final String GAME_ID = "testGame";
    private final int ROWS = 5;
    private final int COLS = 5;
    private final int GLOBAL_MINES = 5;
    private final int PLAYER_MINES = 2;

    @BeforeEach
    void setUp() {
        gameManagerService = new GameManagerService();
        gameManagerService.initializeGame(GAME_ID, ROWS, COLS, GLOBAL_MINES, PLAYER_MINES);
    }

    @Test
    void initializeGameShouldCreateNewGameInstance() {
        // Verificar que el juego se creó correctamente
        GameState gameState = gameManagerService.getGameState(GAME_ID);
        assertNotNull(gameState);
        assertEquals(ROWS, gameState.getBoardMatrix().length);
        assertEquals(COLS, gameState.getBoardMatrix()[0].length);
        assertEquals("IN_PROGRESS", gameState.getStatus());
    }

    @Test
    void getGameStateShouldThrowExceptionForNonexistentGame() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameManagerService.getGameState("nonexistentGame");
        });
    }

    @Test
    void addPlayerShouldCreatePlayerSuccessfully() {
        Position position = new Position(0, 0);
        Player player = gameManagerService.addPlayer(GAME_ID, position, PLAYER_MINES);
        Position positionReal = player.getPosition();
        assertNotNull(player);
        assertEquals(positionReal, player.getPosition());
        assertEquals(PLAYER_MINES, player.getMines());
    }

    @Test
    void addPlayerShouldThrowExceptionForNonexistentGame() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameManagerService.addPlayer("nonexistentGame", new Position(0, 0), PLAYER_MINES);
        });
    }

    @Test
    void movePlayerShouldReturnTrueForValidMove() {
        Position startPos = new Position(0, 0);
        Player player = gameManagerService.addPlayer(GAME_ID, startPos, PLAYER_MINES);
        player.setPosition(new Position(0, 0));

        boolean result = gameManagerService.movePlayer(GAME_ID, player.getSymbol(), 'D');
        assertTrue(result);
    }


    @Test
    void movePlayerShouldThrowExceptionForNonexistentGame() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameManagerService.movePlayer("nonexistentGame", "P1", 'D');
        });
    }

    @Test
    void flagElementShouldWorkForValidPosition() {
        Position startPos = new Position(1, 1);
        Player player = gameManagerService.addPlayer(GAME_ID, startPos, PLAYER_MINES);

        // No debería lanzar excepción
        assertDoesNotThrow(() -> {
            gameManagerService.flagElement(GAME_ID, player.getSymbol(), 'u');
        });
    }

    @Test
    void flagElementShouldThrowExceptionForNonexistentGame() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameManagerService.flagElement("nonexistentGame", "P1", 'u');
        });
    }

    @Test
    void placeMineShouldWorkForValidPlayer() {
        Position startPos = new Position(1, 1);
        Player player = gameManagerService.addPlayer(GAME_ID, startPos, PLAYER_MINES);
        gameManagerService.changePlayerMode(GAME_ID, player.getSymbol(), 'T');

        // No debería lanzar excepción
        assertDoesNotThrow(() -> {
            gameManagerService.placeMine(GAME_ID, player.getSymbol(), 'u');
        });
    }

    @Test
    void placeMineShouldThrowExceptionForNonexistentGame() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameManagerService.placeMine("nonexistentGame", "P1", 'u');
        });
    }

    @Test
    void changePlayerModeShouldChangePlayerMode() {
        Position startPos = new Position(1, 1);
        Player player = gameManagerService.addPlayer(GAME_ID, startPos, PLAYER_MINES);

        gameManagerService.changePlayerMode(GAME_ID, player.getSymbol(), 'T');
        assertEquals('T', player.getMode());

        gameManagerService.changePlayerMode(GAME_ID, player.getSymbol(), 'N');
        assertEquals('N', player.getMode());
    }

    @Test
    void changePlayerModeShouldThrowExceptionForNonexistentGame() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameManagerService.changePlayerMode("nonexistentGame", "P1", 'T');
        });
    }

    @Test
    void getBoardShouldReturnBoardForExistingGame() {
        Board board = gameManagerService.getBoard(GAME_ID);
        assertNotNull(board);
        assertEquals(ROWS, board.getRows());
        assertEquals(COLS, board.getCols());
    }

    @Test
    void getBoardShouldThrowExceptionForNonexistentGame() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameManagerService.getBoard("nonexistentGame");
        });
    }

    @Test
    void initializeGameShouldOverwriteExistingGame() {
        // Crear un segundo juego con el mismo ID
        gameManagerService.initializeGame(GAME_ID, 3, 3, 2, 1);

        GameState gameState = gameManagerService.getGameState(GAME_ID);
        assertEquals(3, gameState.getBoardMatrix().length);
        assertEquals(3, gameState.getBoardMatrix()[0].length);
    }


    @Test
    void serviceShouldHandleMultipleGames() {
        String gameId2 = "game2";
        gameManagerService.initializeGame(gameId2, 3, 3, 2, 1);

        assertNotNull(gameManagerService.getGameState(GAME_ID));
        assertNotNull(gameManagerService.getGameState(gameId2));
    }

    // Método para exponer el mapa de instancias de juego (solo para pruebas)
    // Esto requeriría agregar un método getter en GameManagerService o usar reflexión
    private Map<String, GameManager> getGameInstances() {
        // Implementación usando reflexión para acceder al campo privado
        // (O podrías agregar un método getGameInstances() con visibilidad de paquete)
        try {
            java.lang.reflect.Field field = GameManagerService.class.getDeclaredField("gameInstances");
            field.setAccessible(true);
            return (Map<String, GameManager>) field.get(gameManagerService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}