package com.eci.ARSW.GameService.GameService.service;

import com.eci.ARSW.GameService.GameService.model.*;
import org.springframework.stereotype.Service;

@Service
public class GameManagerService {

    private GameManager gameManager;

    /**
     * Initializes the game with the specified parameters.
     * @param rows of the game grid
     * @param cols of the game grid
     * @param numMinesGlobal total number of mines in the game
     * @param numMinesPerPlayer number of mines each player can place
     */
    public void initializeGame(int rows, int cols, int numMinesGlobal, int numMinesPerPlayer) {
        this.gameManager = new GameManager(rows, cols, numMinesGlobal, numMinesPerPlayer);
    }

    /**
     * Retrieves the current state of the game.
     * @return the current game state
     */
    public GameState getGameState() {
        return gameManager.getGameState();
    }

    /**
     * Adds a player to the game at the specified position with a given number of mines.
     * @param position where the player will be added
     * @param playerMines number of mines the player can place
     * @return the created Player object
     */
    public Player addPlayer(Position position, int playerMines) {
        return gameManager.createPlayer(position, playerMines);
    }

    /**
     * Moves a player in the specified direction.
     * @param playerId of the player to be moved
     * @param direction to move the player
     * @return true if the move was successful, false otherwise
     */
    public boolean movePlayer(String playerId, char direction) {
        return gameManager.movePlayer(playerId, direction);
    }

    /**
     * Places a mine for the specified player at the given coordinates.
     * @param playerId of the player placing the mine
     * @param x coordinate of the mine
     * @param y coordinate of the mine
     */
    public void placeMine(String playerId, int x, int y) {
        gameManager.placeMine(playerId, x, y);
    }

    /**
     * Retrieves the game board.
     * @return the current game board
     */
    public Board getBoard() {
        return gameManager.getBoard();
    }
}

