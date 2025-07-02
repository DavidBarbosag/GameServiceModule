package com.eci.ARSW.GameService.GameService.model;
import com.eci.ARSW.GameService.GameService.model.GameElement;
import com.eci.ARSW.GameService.GameService.model.Mine;
import com.eci.ARSW.GameService.GameService.model.Player;


public class Board {

    private int rows;
    private int cols;
    private GameElement[][] matrix;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new GameElement[rows][cols];
    }

    public GameElement getElementAt(int row, int col) {
        if (isInBounds(row, col)) {
            return matrix[row][col];
        }
        return null;
    }

    public void setElementAt(int row, int col, GameElement element) {
        if (isInBounds(row, col)) {
            matrix[row][col] = element;
        }
    }

    public void removeElementAt(int row, int col) {
        if (isInBounds(row, col)) {
            matrix[row][col] = null;
        }
    }

    public boolean isEmpty(int row, int col) {
        return isInBounds(row, col) && matrix[row][col] == null;
    }

    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public GameElement[][] getMatrix() {
        return matrix;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}