package com.eci.ARSW.GameService.GameService.dto;

public class GameInitDTO {
    private int rows;
    private int cols;
    private int globalMines;
    private int minesPerPlayer;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getGlobalMines() {
        return globalMines;
    }

    public void setGlobalMines(int globalMines) {
        this.globalMines = globalMines;
    }

    public int getMinesPerPlayer() {
        return minesPerPlayer;
    }

    public void setMinesPerPlayer(int minesPerPlayer) {
        this.minesPerPlayer = minesPerPlayer;
    }
}

