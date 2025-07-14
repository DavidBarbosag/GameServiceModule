package com.eci.ARSW.GameService.GameService.model;

import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Represents a Mine in the game.
 * Each mine has a position, state, and symbol.
 * The state can be Enabled (E), Flagged (F), or Deactivated (D).
 */
public class Mine implements GameElement {
    private Position position;
    private char state = 'E'; // E = Enabled, F = flagged, D = Deactivated
    private String symbol;

    /**
     * Constructs a Mine with a given position and default state.
     *
     * @param position the position of the mine
     */
    public Mine(Position position) {
        this.position = position;
        this.state = state;
        this.symbol = "M"; // Default symbol for Mine Element
    }

    /**
     * Returns the position of the mine.
     * @return the position of the mine
     */
    public Position getPosition() { return position; }

    /**
     * Sets the position of the mine.
     * @param position the new position of the mine
     */
    public void setPosition(Position position) { this.position = position; }

    /**
     * Returns the state of the mine.
     * @return the state of the mine
     */
    public char getState() { return state; }

    /**
     * Sets the state of the mine.
     * @param state the new state of the mine (E, F, D)
     */
    public void setState(char state) { this.state = state; }

    /**
     * Returns the symbol representing the mine.
     * The symbol is a combination of the mine's symbol and its state.
     * @return the symbol of the mine
     */
    public String getSymbol() {
        return symbol + state;
    }


}
