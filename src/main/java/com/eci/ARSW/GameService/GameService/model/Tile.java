package com.eci.ARSW.GameService.GameService.model;

public class Tile implements GameElement {
    private boolean revealed;
    private boolean flagged;
    private int adjacentMines;
    private Position position;

    public Tile(int x, int y) {
        this.revealed = false;
        this.flagged = false;
        this.adjacentMines = 0;
        this.position = new Position();
        this.position.setX(x);
        this.position.setY(y);
    }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public char getSymbol() {
        if (!revealed) return flagged ? 'F' : '#';
        return adjacentMines == 0 ? ' ' : Character.forDigit(adjacentMines, 10);
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
}
