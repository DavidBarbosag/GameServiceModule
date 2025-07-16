package com.eci.ARSW.GameService.GameService.model;


/**
 * Represents a tile in the game, which can be revealed, flagged, and has a count of adjacent mines.
 */
public class Tile implements GameElement {
    private boolean revealed;
    private boolean flagged;
    private int adjacentMines;
    private Position position;
    private String symbol;

    /**
     * Constructs a Tile with default values.
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     */
    public Tile(int x, int y) {
        this.revealed = true;
        this.flagged = false;
        this.adjacentMines = 0;
        this.position = new Position();
        this.position.setX(x);
        this.position.setY(y);
    }

    /**
     * Returns the position of the tile.
     * @return
     */
    public Position getPosition() { return position; }


    /**
     * Sets the position of the tile.
     * @param position the new position of the tile
     */
    public void setPosition(Position position) { this.position = position; }

    /**
     * Returns whether the tile is revealed.
     * @return true if the tile is revealed, false otherwise
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * Sets the revealed state of the tile.
     * @param revealed true to reveal the tile, false to hide it
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    /**
     * Returns whether the tile is flagged.
     * @return true if the tile is flagged, false otherwise
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Sets the flagged state of the tile.
     * @param flagged true to flag the tile, false to unflag it
     */
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    /**
     * Returns the number of adjacent mines to this tile.
     * @return the count of adjacent mines
     */
    public int getAdjacentMines() {
        return adjacentMines;
    }

    /**
     * Sets the number of adjacent mines for this tile.
     * @param adjacentMines the count of adjacent mines to set
     */
    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    /**
     * Returns the symbol representation of the tile.
     * The symbol is a string that includes the number of adjacent mines and whether the tile is flagged.
     * @return the symbol of the tile
     */
    public String getSymbol() {
        return "T" + adjacentMines + (flagged ? "F" : "N") + (revealed ? "R" : "H");
    }

    /**
     * Sets the symbol of the tile.
     * @param symbol the new symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

}
