package com.eci.ARSW.GameService.GameService.model;

/**
 * Interface representing a game element with a position and a symbol.
 */
public interface GameElement {
    /**
     * Gets the position of the game element.
     * @return the position of the game element
     */
    Position getPosition();

    /**
     * Sets the position of the game element.
     * @param position the new position to set for the game element
     */
    void setPosition(Position position);

    /**
     * Gets the symbol representing the game element.
     * @return the symbol of the game element
     */
    String getSymbol();
}