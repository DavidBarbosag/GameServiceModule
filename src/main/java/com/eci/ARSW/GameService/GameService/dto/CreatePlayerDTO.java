package com.eci.ARSW.GameService.GameService.dto;

public class CreatePlayerDTO {
    private int x;
    private int y;
    private int mines;

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

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }
}
