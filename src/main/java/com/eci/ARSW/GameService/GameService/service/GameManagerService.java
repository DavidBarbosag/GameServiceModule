package com.eci.ARSW.GameService.GameService.service;

import com.eci.ARSW.GameService.GameService.model.*;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameManagerService {

    private final Map<String, GameManager> gameInstances = new ConcurrentHashMap<>();

    public void initializeGame(String gameId, int rows, int cols, int numMinesGlobal, int numMinesPerPlayer) {
        GameManager game = new GameManager(rows, cols, numMinesGlobal, numMinesPerPlayer);
        game.setId(gameId);
        gameInstances.put(gameId, game);    }

    public GameState getGameState(String gameId) {
        GameManager game = gameInstances.get(gameId);
        if (game == null) throw new IllegalArgumentException("Game with ID " + gameId + " not found");
        return game.getGameState();
    }

    public Player addPlayer(String gameId, Position position, int playerMines) {
        GameManager game = gameInstances.get(gameId);
        if (game == null) throw new IllegalArgumentException("Game with ID " + gameId + " not found");
        return game.createPlayer(position, playerMines);
    }

    public boolean movePlayer(String gameId, String playerId, char direction) {
        GameManager game = gameInstances.get(gameId);
        if (game == null) throw new IllegalArgumentException("Game with ID " + gameId + " not found");
        return game.movePlayer(playerId, direction);
    }

    public void flagElement(String gameId, String playerId, char direction) {
        GameManager game = gameInstances.get(gameId);
        if (game == null) throw new IllegalArgumentException("Game with ID " + gameId + " not found");
        game.flagElement(playerId, direction);
    }

    public void placeMine(String gameId, String playerId, int x, int y) {
        GameManager game = gameInstances.get(gameId);
        if (game == null) throw new IllegalArgumentException("Game with ID " + gameId + " not found");
        game.placeMine(playerId, x, y);
    }

    public Board getBoard(String gameId) {
        GameManager game = gameInstances.get(gameId);
        if (game == null) throw new IllegalArgumentException("Game with ID " + gameId + " not found");
        return game.getBoard();
    }
}
