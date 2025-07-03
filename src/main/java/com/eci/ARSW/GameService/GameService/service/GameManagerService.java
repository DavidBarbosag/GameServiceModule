package com.eci.ARSW.GameService.GameService.service;

import com.eci.ARSW.GameService.GameService.model.*;
import org.springframework.stereotype.Service;

@Service
public class GameManagerService {

    private GameManager gameManager;

    public void initializeGame(int rows, int cols, int numMinesGlobal, int numMinesPerPlayer) {
        this.gameManager = new GameManager(rows, cols, numMinesGlobal, numMinesPerPlayer);
    }

    public GameState getGameState() {
        return gameManager.getGameState();
    }

    public Player addPlayer(Position position, int playerMines) {
        return gameManager.createPlayer(position, playerMines);
    }

    public boolean movePlayer(String playerId, char direction) {
        return gameManager.movePlayer(playerId, direction);
    }

    public void placeMine(String playerId, int x, int y) {
        gameManager.placeMine(playerId, x, y);
    }

    public Board getBoard() {
        return gameManager.getBoard();
    }
}

