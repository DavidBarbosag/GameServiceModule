package com.eci.ARSW.GameService.GameService.model;

/**
 * Represents a position in a 2D space with x and y coordinates.
 */
public class Position {
    private int x;
    private int y;

    public Position() {}

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
