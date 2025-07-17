package com.eci.ARSW.GameService.GameService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Represents a player in the game.
 */
public class Player implements GameElement {


    private String id;
    private Position position;
    private int mines;
    private boolean state; // alive or not
    private char mode; // 'N' = normal, 'T' = tactical (can place bombs)
    private String symbol = "P";
    private int score = 0;

    public Player() {}

    /**
     * Constructs a Player with the specified position and number of mines.
     *
     * @param position the initial position of the player
     * @param mines the initial number of mines the player has
     */
    public Player(Position position, int mines) {
        this.position = position;
        this.mines = mines;
        this.state = true; // Default state is alive
        this.mode = 'N'; // Default mode is normal
    }

    /**
     * Returns the unique identifier of the player.
     * @return the player's ID
     */
    public String getId() { return id; }

    /**
     * Sets the unique identifier of the player.
     * @param id the player's ID
     */
    public void setId(String id) { this.id = id; }

    /**
     * Returns the current position of the player.
     * @return the player's position
     */
    public Position getPosition() { return position; }

    /**
     * Sets the current position of the player.
     * @param position the new position of the player
     */
    public void setPosition(Position position) { this.position = position; }

    /**
     * Returns the number of mines the player has.
     * @return the number of mines
     */
    public int getMines() { return mines; }

    /**
     * Sets the number of mines the player has.
     * @param mines the new number of mines
     */
    public void setMines(int mines) { this.mines = mines; }

    /**
     * Returns whether the player is alive.
     * @return true if the player is alive, false otherwise
     */
    public boolean isState() { return state; }

    /**
     * Sets the state of the player.
     * @param state true if the player is alive, false if dead
     */
    public void setState(boolean state) { this.state = state; }

    /**
     * Returns the current mode of the player.
     * @return 'N' for normal mode, 'T' for tactical mode
     */
    public char getMode() { return mode; }

    /**
     * Sets the mode of the player.
     * @param mode 'N' for normal mode, 'T' for tactical mode
     */
    public void setMode(char mode) { this.mode = mode; }

    /**
     * Returns the symbol representing the player.
     * @return the player's symbol
     */
    public  String getSymbol() { return symbol; }

    /**
     * Sets the symbol representing the player.
     * @param symbol the new symbol for the player
     */
    public void setSymbol(String symbol) { this.symbol = symbol; }

    /**
     * Returns the score of the player.
     * @return the player's score
     */
    public int getScore() { return score; }

    /**
     * Sets the score of the player.
     * @param score the new score for the player
     */
    public void setScore(int score) { this.score = score;}

}
