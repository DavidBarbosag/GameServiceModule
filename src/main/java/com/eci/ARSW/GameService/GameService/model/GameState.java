package com.eci.ARSW.GameService.GameService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Represents the state of a game, including the board matrix, status, players, and mines.
 * This class is used to store and retrieve game states from a MongoDB database.
 */
@Document(collection = "game_states")
public class GameState {

    @Id
    private String id;

    private String gameId;
    private String[][] boardMatrix;
    private String winnerId;
    private String status; // IN_PROGRESS, FINISHED, PAUSED, etc.

    private Date createdAt;
    private Date updatedAt;

    private List<Player> players;
    private List<Mine> mines;

    public GameState() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /**
     * Constructs a GameState with a given board matrix.
     *
     * @param boardMatrix the initial state of the game board
     */
    public GameState(String[][] boardMatrix) {
        this.boardMatrix = boardMatrix;
        this.status = "IN_PROGRESS";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /**
     * Returns the unique identifier of the game state.
     * @return the ID of the game state
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the game state.
     * @param id the new ID of the game state
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the current state of the game board as a matrix.
     * @return
     */
    public String[][] getBoardMatrix() {
        return boardMatrix;
    }

    /**
     * Sets the game board matrix to a new state.
     * @param boardMatrix the new state of the game board
     */
    public void setBoardMatrix(String[][] boardMatrix) {
        this.boardMatrix = boardMatrix;
    }

    /**
     * Gets the current status of the game.
     * @return the status of the game (e.g., IN_PROGRESS, FINISHED, PAUSED)
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the game.
     * @param status the new status of the game
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the list of players in the game.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Sets the list of players in the game.
     * @param players the new list of players
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Returns the list of mines in the game.
     * @return the list of mines
     */
    public List<Mine> getMines() {
        return mines;
    }

    /**
     * Sets the list of mines in the game.
     * @param mines the new list of mines
     */
    public void setMines(List<Mine> mines) {
        this.mines = mines;
    }

    /**
     * Returns the creation date of the game state.
     * @return the creation date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation date of the game state.
     * @param createdAt the new creation date
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the last updated date of the game state.
     * @return the last updated date
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last updated date of the game state.
     * @param updatedAt the new last updated date
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Returns the unique identifier of the game associated with this game state.
     * @return the game ID
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Sets the unique identifier of the game associated with this game state.
     * @param gameId the new game ID
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * Returns the ID of the player who won the game.
     * @return the winner's player ID
     */
    public String getWinnerId() {
        return winnerId;
    }

    /**
     * Sets the ID of the player who won the game.
     * @param winnerId the ID of the winning player
     */
    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }
}

